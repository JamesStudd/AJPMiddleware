/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.MetaAgent;
import Agents.NodeMonitor;
import Agents.Portal;
import Agents.UserAgent;
import Messages.Message;
import Messages.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Testing class
 *
 */
public class Testing {

	/**
	 * Creates 5 linked portals and populates them with user agents. Created
	 * 5 test hashmaps and populated them with links to the MetaAgents we
	 * expect to see from the portals. Tested the 2 maps against each other.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void testingAddresses() throws InterruptedException {

		//Head
		Portal HeadPortal = new Portal("HeadPortal");
		//Tier 2
		Portal T1P2 = new Portal("T1P2", HeadPortal);
		Portal T1P3 = new Portal("T1P3", HeadPortal);

		//Tier 3
		Portal T1P4 = new Portal("T1P4", T1P2);
		Portal T1P5 = new Portal("T1P5", T1P3);

		System.out.println("\n\n");

		//T1P2
		UserAgent T1P2One = new UserAgent("T1P2One", T1P2, HeadPortal);
		UserAgent T1P2Two = new UserAgent("T1P2Two", T1P2, null);
//            //T1P3
		UserAgent T1P3One = new UserAgent("T1P3One", T1P3, null);
//            //T1P4
		UserAgent T1P4One = new UserAgent("T1P4One", T1P4, null);
//            //T1P5
		UserAgent T1P5One = new UserAgent("T1P5One", T1P5, null);

		Thread.sleep(10000);

		HashMap<String, MetaAgent> P2Test = new HashMap<String, MetaAgent>();
		P2Test.put("T1P2One", T1P2One);
		P2Test.put("T1P2Two", T1P2Two);
		P2Test.put("T1P4", T1P4);
		P2Test.put("HeadPortal", HeadPortal);
		P2Test.put("T1P3", HeadPortal);
		P2Test.put("T1P2", T1P2);
		P2Test.put("T1P4One", T1P4);
		P2Test.put("T1P5One", HeadPortal);
		P2Test.put("T1P3One", HeadPortal);
		P2Test.put("T1P5", HeadPortal);

		ConcurrentHashMap<String, MetaAgent> P3Test = new ConcurrentHashMap<String, MetaAgent>();
		P3Test.put("HeadPortal", HeadPortal);
		P3Test.put("T1P5", T1P5);
		P3Test.put("T1P2Two", HeadPortal);
		P3Test.put("T1P3", T1P3);
		P3Test.put("T1P4", HeadPortal);
		P3Test.put("T1P2", HeadPortal);
		P3Test.put("T1P2One", HeadPortal);
		P3Test.put("T1P4One", HeadPortal);
		P3Test.put("T1P5One", T1P5);
		P3Test.put("T1P3One", T1P3One);

		ConcurrentHashMap<String, MetaAgent> P4Test = new ConcurrentHashMap<String, MetaAgent>();
		P4Test.put("T1P5", T1P2);
		P4Test.put("T1P2Two", T1P2);
		P4Test.put("T1P3", T1P2);
		P4Test.put("T1P4One", T1P4One);
		P4Test.put("T1P5One", T1P2);
		P4Test.put("T1P3One", T1P2);
		P4Test.put("T1P2One", T1P2);
		P4Test.put("HeadPortal", T1P2);
		P4Test.put("T1P2", T1P2);

		ConcurrentHashMap<String, MetaAgent> P5Test = new ConcurrentHashMap<String, MetaAgent>();
		P5Test.put("T1P2Two", T1P3);
		P5Test.put("T1P3", T1P3);
		P5Test.put("T1P4", T1P3);
		P5Test.put("T1P2One", T1P3);
		P5Test.put("T1P4One", T1P3);
		P5Test.put("T1P5One", T1P5One);
		P5Test.put("T1P3One", T1P3);
		P5Test.put("HeadPortal", T1P3);
		P5Test.put("T1P2", T1P3);

		assertEquals(T1P2.getRegisteredAddresses(), P2Test);
		assertEquals(T1P3.getRegisteredAddresses(), P3Test);
		assertEquals(T1P4.getRegisteredAddresses(), P4Test);
		assertEquals(T1P5.getRegisteredAddresses(), P5Test);
	}

	/**
	 * Creates 2 user agents on a single portal and sends messages between
	 * them. Checks that the last message received by the node monitor is
	 * the same as one sent.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void usersOnSinglePortal() throws InterruptedException {
		Portal testPortal = new Portal("testPortal");

		UserAgent userOne = new UserAgent("userOne", testPortal, null);
		UserAgent userTwo = new UserAgent("userTwo", testPortal, null);

		NodeMonitor nm = new NodeMonitor("NM1");

		Thread.sleep(500);

		nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", userOne));
		nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", userTwo));

		Thread.sleep(500);

		userOne.passOverAMessage("userTwo", "User one sending a message to user two.");

		userTwo.passOverAMessage("userOne", "User two sending a message to user one.");

		String testMsg1 = "User two sending a message to user one.";
		String testMsg2 = "User one sending a message to user two.";
		Thread.sleep(500);
		System.out.println(nm.getLastMessage("userOne").retrieveMessageItem());
		Thread.sleep(500);
		assertEquals(testMsg2, nm.getLastMessage("userTwo").retrieveMessageItem());
		assertEquals(testMsg1, nm.getLastMessage("userOne").retrieveMessageItem());
	}

	/**
	 * Creates 2 portals and links them together, then creates 2 test maps
	 * which also have the links in them. Tests the stored addresses of the
	 * portals against the test maps.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void portalPortalLink() throws InterruptedException {
		Portal tier1Portal = new Portal("tier1Portal");
		Portal tier2Portal = new Portal("tier2Portal", tier1Portal);

		Thread.sleep(500);

		ConcurrentHashMap<String, MetaAgent> tier1Map = new ConcurrentHashMap<String, MetaAgent>();
		tier1Map.put("tier2Portal", tier2Portal);
		ConcurrentHashMap<String, MetaAgent> tier2Map = new ConcurrentHashMap<String, MetaAgent>();
		tier2Map.put("tier1Portal", tier1Portal);

		assertEquals(tier1Portal.getRegisteredAddresses(), tier1Map);
		assertEquals(tier2Portal.getRegisteredAddresses(), tier2Map);
	}

	/**
	 * Creates 2 portals and links them together, then populates each portal
	 * with a user agent. Sends a message from 1 user to another across
	 * portals. Checks the last message in the node monitor is the same as
	 * expected.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void sendMessageBetweenPortals() throws InterruptedException {
		Portal portalOne = new Portal("portalOne");
		Portal portalTwo = new Portal("portalTwo", portalOne);

		UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
		UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, null);
		NodeMonitor nm = new NodeMonitor("NM1");

		Thread.sleep(500);

		nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", portalTwoAgent));

		Thread.sleep(500);

		portalOneAgent.passOverAMessage("portalTwoAgent", "Passing a message to a user agent in a different portal.");
		String expected = "Passing a message to a user agent in a different portal.";

		Thread.sleep(500);

		assertEquals(expected, nm.getLastMessage("portalTwoAgent").retrieveMessageItem());

	}

	/**
	 * Creates a portal and populates it with one user. Sends a message from
	 * the user to another user agent that does not yet exist. Checks in the
	 * node monitor that the sender received an error message.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void sendMessageToUserThatIsNotCreated() throws InterruptedException {
		Portal testPortal = new Portal("testPortal");

		UserAgent agentOne = new UserAgent("agentOne", testPortal, null);

		NodeMonitor nm = new NodeMonitor("NM1");

		Thread.sleep(500);

		nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", agentOne));

		Thread.sleep(500);

		agentOne.passOverAMessage("agentTwo", "Passing a message to a user agent in a different portal.");

		Thread.sleep(500);

		assertEquals(1, testPortal.getLostMessages().getSize());
	}

	/**
	 * Creates 2 portals and populates them with a user agent. The second
	 * portal's user agent is scoped just to it's parent portal. Attempts to
	 * send a message from the first portal's user to the second. Checks
	 * that the node monitor received an error message equal to what we
	 * expect.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void sendMessageToUserOutOfScope() throws InterruptedException {
		Portal portalOne = new Portal("portalOne");
		Portal portalTwo = new Portal("portalTwo", portalOne);

		UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
		UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, portalTwo);

		Thread.sleep(500);

		portalOneAgent.passOverAMessage("portalTwoAgent", "Passing a message to a user agent outside of scope.");

		Thread.sleep(500);

		assertEquals(1, portalOne.getLostMessages().getSize());
	}

	/**
	 * Creates a portal and populates it with a user agent. Sends a message
	 * from user agent to another user agent that does not yet exist. Test
	 * then creates the user agent the message was intended for and checks
	 * if it receives the message through the node monitor.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void creatingUserThatHasMessageWaitingInLostProperty() throws InterruptedException {
		Portal portalOne = new Portal("portalOne");
		UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
		NodeMonitor nm = new NodeMonitor("NM1");

		Thread.sleep(500);

		agentOne.passOverAMessage("agentTwo", "Passing a message to a user agent outside of scope.");

		Thread.sleep(500);
		UserAgent agentTwo = new UserAgent("agentTwo", portalOne, null);
		Thread.sleep(500);
		assertEquals(0, portalOne.getLostMessages().getSize());
	}

	/**
	 * Creates 2 portals and populates them with a user agent on each. Then
	 * introduces the 2 portals and checks that one of them has references
	 * to all of the other's addresses against a test map created.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void introducingTwoPortals() throws InterruptedException {
		Portal portalOne = new Portal("portalOne");
		Portal portalTwo = new Portal("portalTwo");
		UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
		UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, null);

		Thread.sleep(500);

		portalTwo.setParent(portalOne);

		Thread.sleep(500);

		ConcurrentHashMap<String, MetaAgent> testMap = new ConcurrentHashMap<String, MetaAgent>();
		testMap.put("portalOneAgent", portalOneAgent);
		testMap.put("portalTwo", portalTwo);
		testMap.put("portalTwoAgent", portalTwo);
		testMap.put("portalOne", portalOne);

		Thread.sleep(500);

		assertEquals(testMap, portalOne.getRegisteredAddresses());
	}

	/**
	 * This test creates a large tree of agents and portals and sends thousands of messages across the network
	 * and is there to test if the network can handle it
	 */
	@Test
	public void bombardingAgentsWith1000sOfMessages() {

		//Master portal
		Portal headPortal = new Portal("headPortal");

		NodeMonitor monitor = new NodeMonitor("mainMonitor");

		monitor.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, headPortal.toString(), monitor.toString(), headPortal));

		//First tier, seperating the tree
		Portal tier1Left = new Portal("tier1Left", headPortal);
		Portal tier1Right = new Portal("tier1Right", headPortal);

		//Generating portals for left side of tree
		Portal leftPortal1 = new Portal("leftPortal1", tier1Left);
		Portal leftPortal2 = new Portal("leftPortal2", tier1Left);
		Portal leftPortal3 = new Portal("leftPortal3", tier1Left);
		Portal leftPortal4 = new Portal("leftPortal4", tier1Left);

		//Generating portals for right side of tree
		Portal rightPortal1 = new Portal("rightPortal1", tier1Right);
		Portal rightPortal2 = new Portal("rightPortal2", tier1Right);
		Portal rightPortal3 = new Portal("rightPortal3", tier1Right);
		Portal rightPortal4 = new Portal("rightPortal4", tier1Right);

		//Creating user agents for left side of tree:
		//Tier 1 Left:
		UserAgent tier1LeftAgent1 = new UserAgent("tier1LeftAgent1", tier1Left, null);
		UserAgent tier1LeftAgent2 = new UserAgent("tier1LeftAgent2", tier1Left, null);

		//Generating user agents for the children portals (left side)
		UserAgent leftPortal1Agent1 = new UserAgent("leftPortal1Agent1", leftPortal1, null);
		UserAgent leftPortal1Agent2 = new UserAgent("leftPortal1Agent2", leftPortal1, null);
		UserAgent leftPortal1Agent3 = new UserAgent("leftPortal1Agent3", leftPortal1, null);
		UserAgent leftPortal1Agent4 = new UserAgent("leftPortal1Agent4", leftPortal1, null);

		UserAgent leftPortal2Agent1 = new UserAgent("leftPortal2Agent1", leftPortal2, null);
		UserAgent leftPortal2Agent2 = new UserAgent("leftPortal2Agent2", leftPortal2, null);
		UserAgent leftPortal2Agent3 = new UserAgent("leftPortal2Agent3", leftPortal2, null);
		UserAgent leftPortal2Agent4 = new UserAgent("leftPortal2Agent4", leftPortal2, null);

		UserAgent leftPortal3Agent1 = new UserAgent("leftPortal3Agent1", leftPortal3, null);
		UserAgent leftPortal3Agent2 = new UserAgent("leftPortal3Agent2", leftPortal3, null);
		UserAgent leftPortal3Agent3 = new UserAgent("leftPortal3Agent3", leftPortal3, null);
		UserAgent leftPortal3Agent4 = new UserAgent("leftPortal3Agent4", leftPortal3, null);

		UserAgent leftPortal4Agent1 = new UserAgent("leftPortal4Agent1", leftPortal4, null);
		UserAgent leftPortal4Agent2 = new UserAgent("leftPortal4Agent2", leftPortal4, null);
		UserAgent leftPortal4Agent3 = new UserAgent("leftPortal4Agent3", leftPortal4, null);
		UserAgent leftPortal4Agent4 = new UserAgent("leftPortal4Agent4", leftPortal4, null);

		//Creating user agents for right side of tree:
		//Tier 1 Right:
		UserAgent tier1RightAgent1 = new UserAgent("tier1RightAgent1", tier1Right, null);
		UserAgent tier1RightAgent2 = new UserAgent("tier1RightAgent2", tier1Right, null);

		//Generating user agents for the children portals (right side)
		UserAgent rightPortal1Agent1 = new UserAgent("rightPortal1Agent1", rightPortal1, null);
		UserAgent rightPortal1Agent2 = new UserAgent("rightPortal1Agent2", rightPortal1, null);
		UserAgent rightPortal1Agent3 = new UserAgent("rightPortal1Agent3", rightPortal1, null);
		UserAgent rightPortal1Agent4 = new UserAgent("rightPortal1Agent4", rightPortal1, null);

		UserAgent rightPortal2Agent1 = new UserAgent("rightPortal2Agent1", rightPortal2, null);
		UserAgent rightPortal2Agent2 = new UserAgent("rightPortal2Agent2", rightPortal2, null);
		UserAgent rightPortal2Agent3 = new UserAgent("rightPortal2Agent3", rightPortal2, null);
		UserAgent rightPortal2Agent4 = new UserAgent("rightPortal2Agent4", rightPortal2, null);

		UserAgent rightPortal3Agent1 = new UserAgent("rightPortal3Agent1", rightPortal3, null);
		UserAgent rightPortal3Agent2 = new UserAgent("rightPortal3Agent2", rightPortal3, null);
		UserAgent rightPortal3Agent3 = new UserAgent("rightPortal3Agent3", rightPortal3, null);
		UserAgent rightPortal3Agent4 = new UserAgent("rightPortal3Agent4", rightPortal3, null);

		UserAgent rightPortal4Agent1 = new UserAgent("rightPortal4Agent1", rightPortal4, null);
		UserAgent rightPortal4Agent2 = new UserAgent("rightPortal4Agent2", rightPortal4, null);
		UserAgent rightPortal4Agent3 = new UserAgent("rightPortal4Agent3", rightPortal4, null);
		UserAgent rightPortal4Agent4 = new UserAgent("rightPortal4Agent4", rightPortal4, null);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			Logger.getLogger(Testing.class.getName()).log(Level.SEVERE, null, ex);
		}

		ArrayList<UserAgent> agents = new ArrayList();
		agents.add(leftPortal1Agent1);
		agents.add(leftPortal1Agent2);
		agents.add(leftPortal1Agent3);
		agents.add(leftPortal1Agent4);
		agents.add(leftPortal2Agent1);
		agents.add(leftPortal2Agent2);
		agents.add(leftPortal2Agent3);
		agents.add(leftPortal2Agent4);
		agents.add(leftPortal3Agent1);
		agents.add(leftPortal3Agent2);
		agents.add(leftPortal3Agent3);
		agents.add(leftPortal3Agent4);
		agents.add(leftPortal4Agent1);
		agents.add(leftPortal4Agent2);
		agents.add(leftPortal4Agent3);
		agents.add(leftPortal4Agent4);
		agents.add(rightPortal1Agent1);
		agents.add(rightPortal1Agent2);
		agents.add(rightPortal1Agent3);
		agents.add(rightPortal1Agent4);
		agents.add(rightPortal2Agent1);
		agents.add(rightPortal2Agent2);
		agents.add(rightPortal2Agent3);
		agents.add(rightPortal2Agent4);
		agents.add(rightPortal3Agent1);
		agents.add(rightPortal3Agent2);
		agents.add(rightPortal3Agent3);
		agents.add(rightPortal3Agent4);
		agents.add(rightPortal4Agent1);
		agents.add(rightPortal4Agent2);
		agents.add(rightPortal4Agent3);
		agents.add(rightPortal4Agent4);

		Random ran = new Random();

		for (int i = 0; i < 100000; i++) {
			UserAgent sender = agents.get(ran.nextInt(agents.size()));
			UserAgent reciever;
			do {

				reciever = agents.get(ran.nextInt(agents.size()));
			} while (reciever == sender);
			sender.passOverAMessage(reciever.toString(), "message number " + i + " to " + reciever.toString());
		}

	}

}
