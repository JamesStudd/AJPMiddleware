/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareproject;

import Agents.MetaAgent;
import Agents.NodeMonitor;
import Agents.Portal;
import Agents.UserAgent;
import java.util.HashMap;

/**
 *
 * @author Sean
 */
public class MiddlewareProject {

	/**
	 * @param args the command line arguments
	 */
    
    
         
	public static void main(String[] args) throws InterruptedException {
            
            //public Portal(String name, MetaAgent parent){
            //public UserAgent(String name, MetaAgent parent, MetaAgent scope) {
            
            //Constructing first tree:
            //Head
            Portal HeadPortal = new Portal("HeadPortal");
            //Tier 2
            Portal T1P2 = new Portal("T1P2", HeadPortal);
            Portal T1P3 = new Portal("T1P3", HeadPortal);
	    
            //Tier 3
            Portal T1P4 = new Portal("T1P4", T1P2);
            Portal T1P5 = new Portal("T1P5", T1P3);
	    Portal P6 = new Portal("6", null);
	    Portal p7 = new Portal("7", P6);
            NodeMonitor nm = new NodeMonitor("NM1");
            T1P5.addMonitor(nm);
            //Portal T1P5 = new Portal("Tree 1, Portal 5", T1P2);
           // Portal T1P6 = new Portal("Tree 1, Portal 6", T1P3);
           // Portal T1P7 = new Portal("Tree 1, Portal 7", T1P3);
           
	    /**
            //Constructing second tree:
            //Head
            Portal T2P1 = new Portal("Tree 2, Portal 1");
            //Tier 2
            Portal T2P2 = new Portal("Tree 2, Portal 2", T2P1);
            Portal T2P3 = new Portal("Tree 2, Portal 3", T2P1);
            //Tier 3
            Portal T2P4 = new Portal("Tree 2, Portal 4", T2P2);
            Portal T2P5 = new Portal("Tree 2, Portal 5", T2P2);
            Portal T2P6 = new Portal("Tree 2, Portal 6", T2P3);
            Portal T2P7 = new Portal("Tree 2, Portal 7", T2P3);
              */ 
            //STILL NEED TO SORT OUT WHO IS SCOPED TO WHERE
            //Populating Portals in Tree 1
            //T1P2

           UserAgent T1P2One = new UserAgent("T1P2One", T1P2, HeadPortal);
           UserAgent T1P2Two = new UserAgent("T1P2Two", T1P2, null);
//            //T1P3
           UserAgent T1P3One = new UserAgent("T1P3One", T1P3, null);
           
//
//	    
//            //T1P4
           UserAgent T1P4One = new UserAgent("T1P4One", T1P4, null);
//            UserAgent T1P4Two = new UserAgent("T1P4Two", T1P4, T1P3);
//            UserAgent T1P4Three = new UserAgent("T1P4Three", T1P4, T1P2);
//            UserAgent T1P4Four = new UserAgent("T1P4Four", T1P4, null);
//            //T1P5
           UserAgent T1P5One = new UserAgent("T1P5One", T1P5, T1P5);
           
	   UserAgent a = new UserAgent("a", p7, T1P3);
	   UserAgent b = new UserAgent("b", p7, null);
           
//            UserAgent T1P5Two = new UserAgent("T1P5Two", T1P5, T1P2);
//            UserAgent T1P5Three = new UserAgent("T1P5Three", T1P5, T1P5);
//            UserAgent T1P5Four = new UserAgent("T1P5Four", T1P5, null);
//            //T1P6
//            UserAgent T1P6One = new UserAgent("T1P6One", T1P6, T1P6);
//            UserAgent T1P6Two = new UserAgent("T1P6Two", T1P6, T1P3);
//            UserAgent T1P6Three = new UserAgent("T1P6Three", T1P6, T1P2);
//            UserAgent T1P6Four = new UserAgent("T1P6Four", T1P6, null);
//            //T1P7
//            UserAgent T1P7One = new UserAgent("T1P7One", T1P7, T1P7);
//            UserAgent T1P7Two = new UserAgent("T1P7Two", T1P7, T1P3);
//            UserAgent T1P7Three = new UserAgent("T1P7Three", T1P7, T1P2);
//            UserAgent T1P7Four = new UserAgent("T1P7Four", T1P7, null);
//	   
            System.out.println("\n\n");
    
           Thread.sleep(5000);
                System.out.println("Displaying Tree 1:");
                HeadPortal.showAddresses();
		System.out.println("");
		T1P2.showAddresses();
		System.out.println("");
		T1P3.showAddresses();
		System.out.println("");
		T1P4.showAddresses();
                System.out.println("");
		T1P5.showAddresses();
		System.out.println("");
		p7.showAddresses();
		System.out.println("");
		P6.showAddresses();


		P6.setParent(T1P5);

	Thread.sleep(1000);

 		System.out.println("Displaying Tree 1: take 2");
                HeadPortal.showAddresses();
		System.out.println("");
		T1P2.showAddresses();
		System.out.println("");
		T1P3.showAddresses();
		System.out.println("");
		T1P4.showAddresses();
                System.out.println("");
		T1P5.showAddresses();
		System.out.println("");
		p7.showAddresses();
		System.out.println("");
		P6.showAddresses();
		
//                System.out.println("\n");
//
//
//                Thread.sleep(5000);
//
//                T1P3.showAddresses();
//                
//                
//                System.out.println("\n\n\n\n");
//Literally have no idea where this getScope bug is coming from now
//Also the thread in which the error happens seems to change between runs
//                
//            T1P2One.passOverAMessage("T1P2Two", "Test message between shared portal 1");
//            T1P4One.passOverAMessage("T1P3One", "Test message sent one level up 2");
//            T1P4One.passOverAMessage("T1P5One", "Test message sent along a level 3");
//            T1P5One.passOverAMessage("T1P5Five", "Error message, will not send until user is created. 4");
//            T1P3Two.passOverAMessage("T1P2One", "Error message, should be outside of T1P3 scope 5");
//           
//           Thread.sleep(5000);
//           
//           UserAgent T1P5Five = new UserAgent("T1P5Five", T1P5, null);
                      
            
            
//              //Chris' example
//		Portal portalOne = new Portal("Portal 1");
//		Portal portalTwo = new Portal("portal2", portalOne);
//		Portal portalThree = new Portal("portal3", portalOne);
//		Portal portalFive = new Portal("portal5", null);
//		Portal portalFour = new Portal("portal4", portalFive);
//
//		UserAgent agentOne = new UserAgent("agentOne", portalOne, null);
//		UserAgent agentTwo = new UserAgent("Agent2", portalOne, null);
//
//		UserAgent agentThree = new UserAgent("Agent3", portalTwo, portalTwo);
//		UserAgent agentFour = new UserAgent("Agent4", portalTwo, null);
//		UserAgent agentFive = new UserAgent("Agent5", portalThree, portalOne);
//		UserAgent agentSix = new UserAgent("Agent6", portalThree, null);
//
//		UserAgent agenta = new UserAgent("Agenta", portalFour, null);
//		UserAgent agentb = new UserAgent("Agentb", portalFour, portalFive);
//		UserAgent agentc = new UserAgent("Agentc", portalFour, portalFour);
//
//		
//		agentOne.passOverAMessage("Agent2", "first message");
//		agentc.passOverAMessage("Agenta", "2nd message");
//              ///LOST MSG TESTING, WORKS AFTER INTRODUCTION
//		agenta.passOverAMessage("agentOne", "this message should come through as lost");
//
//		Thread.sleep(5000);
//		System.out.println("Showing portal one");
//		portalOne.showAddresses();
//		System.out.println("\n");
//		portalTwo.showAddresses();
//		System.out.println("");
//		portalThree.showAddresses();
//		System.out.println("");
//		portalFour.showAddresses();
//		System.out.println("");
//		portalFive.showAddresses();
//
//
//		//Introducing portals 
//		portalFive.setParent(portalOne);
//
//		Thread.sleep(5000);
//		System.out.println("Showing portal one");
//		portalOne.showAddresses();
//		System.out.println("\n");
//		portalTwo.showAddresses();
//		System.out.println("");
//		portalThree.showAddresses();
//		System.out.println("");
//		portalFour.showAddresses();
//		System.out.println("");
//		portalFive.showAddresses();
	}

}
