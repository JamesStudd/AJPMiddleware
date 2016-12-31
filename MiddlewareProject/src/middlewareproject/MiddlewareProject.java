/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareproject;

import Agents.MetaAgent;
import Agents.Portal;
import Agents.UserAgent;
import Messages.Message;
import Messages.MessageType;
import Messages.WrongObjectTypeForMessageException;

/**
 *
 * @author Sean
 */
public class MiddlewareProject {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO code application logic here

		Portal portalOne = new Portal("portal1");
		Portal portalTwo = new Portal("portal2", portalOne);
		Portal portalThree = new Portal("portal3", portalOne);
		UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
		UserAgent agentTwo = new UserAgent("Agent2", portalOne, null);

		UserAgent agentThree = new UserAgent("Agent3", portalTwo, portalTwo);
		UserAgent agentFour = new UserAgent("Agent4", portalTwo, null);
		UserAgent agentFive = new UserAgent("Agent5", portalThree, portalOne);
		UserAgent agentSix = new UserAgent("Agent6", portalThree, null);

		portalOne.addToQueue(new Message<String>(MessageType.PASS_MESSAGE, agentOne.toString(), agentTwo.toString(), "I am the first message"));

		Thread.sleep(5000);
		System.out.println("Showing portal one");
		portalOne.showAddresses();
		System.out.println("\n");
		portalTwo.showAddresses();
		System.out.println("");
		portalThree.showAddresses();
	}

}
