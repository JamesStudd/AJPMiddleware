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
		UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
		UserAgent agentTwo = new UserAgent("Agent2", portalOne, null);

		UserAgent agentThree = new UserAgent("Agent3", portalTwo, portalTwo);
		UserAgent agentFour = new UserAgent("Agent4", portalTwo, null);
		//portalOne.addToQueue(new Message<UserAgent>(MessageType.ADD_NODE, "root", portalOne.toString(), agentOne));
		//portalOne.addToQueue(new Message<UserAgent>(MessageType.ADD_NODE, "root", portalOne.toString(), agentTwo));
		portalOne.addToQueue(new Message<String>(MessageType.PASS_MESSAGE, agentOne.toString(), agentTwo.toString(), "I am the first message"));

		Thread.sleep(5000);
		portalOne.showAddresses();
	}

}
