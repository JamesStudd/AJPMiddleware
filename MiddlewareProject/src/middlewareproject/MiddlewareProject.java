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
//Head
            Portal HeadPortal = new Portal("HeadPortal", null);
            //Tier 2
            Portal T1P2 = new Portal("T1P2", HeadPortal);
            Portal T1P3 = new Portal("T1P3", HeadPortal);
	    
            //Tier 3
            Portal T1P4 = new Portal("T1P4", T1P2);
            Portal T1P5 = new Portal("T1P5", T1P3);
            
            System.out.println("\n\n");
            
              //T1P2
           UserAgent T1P2One = new UserAgent("T1P2One", T1P2, null);
           UserAgent T1P2Two = new UserAgent("T1P2Two", T1P2, null);
//            //T1P3
           UserAgent T1P3One = new UserAgent("T1P3One", T1P3, null);          	    
//            //T1P4
           UserAgent T1P4One = new UserAgent("T1P4One", T1P4, null);
//            //T1P5
           UserAgent T1P5One = new UserAgent("T1P5One", T1P5, null);
            
            Thread.sleep(1000);


	    HeadPortal.showAddresses();
	    T1P3.showAddresses();
	   
	}

}
