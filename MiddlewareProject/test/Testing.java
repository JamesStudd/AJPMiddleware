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
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing class
 *
 */
public class Testing {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }
    
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
            P4Test.put("T1P2Two" ,T1P2);
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

	    T1P2.showAddresses();
	    System.out.println(T1P2.getRegisteredAddresses());
            
            
           
            
           assertEquals(T1P2.getRegisteredAddresses(), P2Test);
            assertEquals(T1P3.getRegisteredAddresses(), P3Test);
            assertEquals(T1P4.getRegisteredAddresses(), P4Test);
            assertEquals(T1P5.getRegisteredAddresses(), P5Test);
}

    @Test
    public void usersOnSinglePortal() throws InterruptedException { 
        Portal testPortal = new Portal("testPortal");
        
        UserAgent userOne = new UserAgent("userOne", testPortal, null);
        UserAgent userTwo = new UserAgent("userTwo", testPortal, null);
        UserAgent userThree = new UserAgent("userThree", testPortal, null);
        
        Thread.sleep(2000);
        
        userOne.passOverAMessage("userTwo", "User one sending a message to user two.");
        userOne.passOverAMessage("userThree", "User one sending a message to user three.");
        
        userTwo.passOverAMessage("userOne", "User two sending a message to user one.");
        userTwo.passOverAMessage("userThree", "User two sending a message to user three");
        
        userThree.passOverAMessage("userOne", "User three sending a message to user one.");
        userThree.passOverAMessage("userTwo", "User three sending a message to user two.");
        

    }
    
    @Test
    public void portalPortalLink() throws InterruptedException {
        Portal tier1Portal = new Portal("tier1Portal");
        Portal tier2Portal = new Portal("tier2Portal", tier1Portal);
        
        Thread.sleep(500);
        
        tier1Portal.showAddresses();
    }
    
   @Test
    public void sendMessageBetweenPortals() throws InterruptedException {
        
	    System.out.println("i am here \n\n\n\n\n");
	    System.out.println(outContent.toString() + "above \n\n\n\n");
	    System.out.println(outContent);
            Portal portalOne = new Portal("portalOne");
            Portal portalTwo = new Portal("portalTwo", portalOne);
            
            UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
            UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, null);
            
            Thread.sleep(500);
            
            portalTwoAgent.passOverAMessage("portalOneAgent", "Passing a message to a user agent in a different portal.");
            
            Thread.sleep(500);
            
	    
            String expected = "Passing a message to a user agent in a different portal.";
            
            Thread.sleep(500);
            
   //         assertEquals(expected, outContent.toString());

    }
    
    @Test
    public void sendMessageToUserThatIsNotCreated() throws InterruptedException {
            Portal portalOne = new Portal("portalOne");
            Portal portalTwo = new Portal("portalTwo", portalOne);
            
            UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
            
            Thread.sleep(500);
            
            portalOneAgent.passOverAMessage("portalTwoAgent", "Passing a message to a user agent in a different portal.");
            
            Thread.sleep(500);
    }
    
    @Test
    public void sendMessageToUserOutOfScope() throws InterruptedException {
            Portal portalOne = new Portal("portalOne");
            Portal portalTwo = new Portal("portalTwo", portalOne);
                       
            UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
            UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, portalTwo);
            
            Thread.sleep(500);
            
            portalOneAgent.passOverAMessage("portalTwoAgent", "Passing a message to a user agent outside of scope.");
            
            Thread.sleep(500);
    }
    
    @Test
    public void creatingUserThatHasMessageWaitingInLostProperty() throws InterruptedException {
            Portal portalOne = new Portal("portalOne");
            UserAgent agentOne = new UserAgent("agentOne", portalOne, null);

            
            Thread.sleep(500);
            
            agentOne.passOverAMessage("agentTwo", "Passing a message to a user agent outside of scope.");
            
            Thread.sleep(500);
            
            UserAgent agentTwo = new UserAgent("agentTwo", portalOne, null);                      
    }
    
    
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
    
    @Test
    public void defaultNodeMonitor() throws InterruptedException {
            Portal portalOne = new Portal("portalOne");
            UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
            UserAgent agentTwo = new UserAgent("agentTwo", portalOne, null);

            
            Thread.sleep(500);
            
            agentOne.passOverAMessage("agentTwo", "Node monitor test message.");
                        
            Thread.sleep(500);
            
            NodeMonitor nm = new NodeMonitor("NM1");
	    nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", agentTwo));
            
                 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    }
