/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareproject;

import Agents.NodeMonitor;
import Agents.Portal;
import Agents.UserAgent;
import Messages.Message;
import Messages.MessageType;

/**
 *
 * @author Sean
 */
public class MiddlewareProject {

	/**
	 * @param args the command line arguments
	 */
    
    
         
	public static void main(String[] args) throws InterruptedException {
            
           /* 
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

	    //Adding in some user agents
           UserAgent T1P2One = new UserAgent("T1P2One", T1P2, HeadPortal);
           UserAgent T1P2Two = new UserAgent("T1P2Two", T1P2, null);
           UserAgent T1P3One = new UserAgent("T1P3One", T1P3, null);
           UserAgent T1P4One = new UserAgent("T1P4One", T1P4, null);
           UserAgent T1P5One = new UserAgent("T1P5One", T1P5, T1P5);

	   nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", P6));
	   UserAgent a = new UserAgent("a", p7, T1P3);
	   UserAgent b = new UserAgent("b", p7, null);
	   nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", T1P5));
	   b.passOverAMessage("T1P2One", "I am a message that will go through lost property");
	   System.out.println("P7 before parent introduction");
	   Thread.sleep(3000);
	   p7.showAddresses();

	   //Introduces two parents
	   P6.setParent(T1P5);

	   Thread.sleep(3000);
	   System.out.println("P7 should now have everyone's addresses and the lost message should come through");
	   p7.showAddresses();
           System.out.println("\n\n");

*/
Portal portalOne = new Portal("portalOne");
            Portal portalTwo = new Portal("portalTwo", portalOne);
            
            UserAgent portalOneAgent = new UserAgent("portalOneAgent", portalOne, null);
            UserAgent portalTwoAgent = new UserAgent("portalTwoAgent", portalTwo, null);
            //NodeMonitor nm = new NodeMonitor("NM1");
            NodeMonitor nm2 = new NodeMonitor("NM2");
        
        
            //nm.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM1", portalTwoAgent));
            nm2.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, "", "NM2", portalTwoAgent));
            Thread.sleep(1500);
            
            portalOneAgent.passOverAMessage("portalTwoAgent", "Passing a message to a user agent in a different portal.");
            String expected = "Passing a message to a user agent in a different portal.";
            Thread.sleep(2000);
            //System.out.println(nm.getLastMessage("portalTwoAgent").retrieveMessageItem() + "1");
            System.out.println(nm2.getLastMessage("portalTwoAgent").retrieveMessageItem());
            
            //Thread.sleep(500);
            
	    //System.out.println(nm2.getLastMessage("portalTwoAgent").retrieveMessageItem());
 //           assertEquals(expected, nm2.getLastMessage("portalTwoAgent").retrieveMessageItem());

}
}
