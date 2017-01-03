/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareproject;

import Agents.Portal;
import Agents.UserAgent;

/**
 *
 * @author Sean
 */
public class MiddlewareProject {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {

		Portal portalOne = new Portal("portal1");
		Portal portalTwo = new Portal("portal2", portalOne);
		Portal portalThree = new Portal("portal3", portalOne);
		Portal portalFive = new Portal("portal5", null);
		Portal portalFour = new Portal("portal4", portalFive);

		UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
		UserAgent agentTwo = new UserAgent("Agent2", portalOne, null);

		UserAgent agentThree = new UserAgent("Agent3", portalTwo, portalTwo);
		UserAgent agentFour = new UserAgent("Agent4", portalTwo, null);
		UserAgent agentFive = new UserAgent("Agent5", portalThree, portalOne);
		UserAgent agentSix = new UserAgent("Agent6", portalThree, null);

		UserAgent agenta = new UserAgent("Agenta", portalFour, null);
		UserAgent agentb = new UserAgent("Agentb", portalFour, portalFive);
		UserAgent agentc = new UserAgent("Agentc", portalFour, portalFour);

		
		agentOne.passOverAMessage("Agent2", "first message");
		agentc.passOverAMessage("Agenta", "2nd message");
		agenta.passOverAMessage("agentOne", "this message should come through as lost");

		Thread.sleep(5000);
		System.out.println("Showing portal one");
		portalOne.showAddresses();
		System.out.println("\n");
		portalTwo.showAddresses();
		System.out.println("");
		portalThree.showAddresses();
		System.out.println("");
		portalFour.showAddresses();
		System.out.println("");
		portalFive.showAddresses();


		//Introducing portals 
		portalFive.setParent(portalOne);

		Thread.sleep(5000);
		System.out.println("Showing portal one");
		portalOne.showAddresses();
		System.out.println("\n");
		portalTwo.showAddresses();
		System.out.println("");
		portalThree.showAddresses();
		System.out.println("");
		portalFour.showAddresses();
		System.out.println("");
		portalFive.showAddresses();
	}

}
