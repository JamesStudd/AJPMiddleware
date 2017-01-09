/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.Portal;
import Agents.MetaAgent;
import Agents.NodeMonitor;
import Agents.UserAgent;
import Messages.Message;
import Messages.MessageType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sean
 */
public class ScenarioTesting {
    
    public ScenarioTesting() {
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
                
                String expected = "Displaying Tree 1:\n" +
"Showing addresses for HeadPortal:\n" +
"HeadPortal : HeadPortal\n" +
"T1P5 : T1P3\n" +
"T1P2Two : T1P2\n" +
"T1P3 : T1P3\n" +
"T1P4 : T1P2\n" +
"T1P2 : T1P2\n" +
"T1P4One : T1P2\n" +
"T1P5One : T1P3\n" +
"T1P3One : T1P3\n" +
"T1P2One : T1P2\n" +
"Showing Children\n" +
"T1P2\n" +
"T1P3\n" +
"\n" +
"Showing addresses for T1P2:\n" +
"HeadPortal : HeadPortal\n" +
"T1P5 : HeadPortal\n" +
"T1P2Two : T1P2Two\n" +
"T1P3 : HeadPortal\n" +
"T1P4 : T1P4\n" +
"T1P2 : T1P2\n" +
"T1P4One : T1P4\n" +
"T1P5One : HeadPortal\n" +
"T1P3One : HeadPortal\n" +
"T1P2One : T1P2One\n" +
"Showing Children\n" +
"T1P2Two\n" +
"T1P4\n" +
"T1P2One\n" +
"\n" +
"Showing addresses for T1P3:\n" +
"HeadPortal : HeadPortal\n" +
"T1P5 : T1P5\n" +
"T1P2Two : HeadPortal\n" +
"T1P3 : T1P3\n" +
"T1P4 : HeadPortal\n" +
"T1P2 : HeadPortal\n" +
"T1P4One : HeadPortal\n" +
"T1P5One : T1P5\n" +
"T1P3One : T1P3One\n" +
"Showing Children\n" +
"T1P5\n" +
"T1P3One\n" +
"\n" +
"Showing addresses for T1P4:\n" +
"T1P5 : T1P2\n" +
"T1P2Two : T1P2\n" +
"T1P3 : T1P2\n" +
"T1P4One : T1P4One\n" +
"T1P5One : T1P2\n" +
"T1P3One : T1P2\n" +
"T1P2One : T1P2\n" +
"HeadPortal : T1P2\n" +
"T1P2 : T1P2\n" +
"Showing Children\n" +
"T1P4One\n" +
"\n" +
"Showing addresses for T1P5:\n" +
"T1P2Two : T1P3\n" +
"T1P3 : T1P3\n" +
"T1P4 : T1P3\n" +
"T1P4One : T1P3\n" +
"T1P5One : T1P5One\n" +
"T1P3One : T1P3\n" +
"HeadPortal : T1P3\n" +
"T1P2 : T1P3\n" +
"Showing Children\n" +
"T1P5One";
                
                // Create a stream to hold the output
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    // IMPORTANT: Save the old System.out!
    PrintStream old = System.out;
    // Tell Java to use your special stream
    System.setOut(ps);
    // Put things back
    System.out.flush();
    System.setOut(old);
    // Show what happened
    System.out.println("Here: " + baos.toString());
    
    Assert.assertEquals(expected, baos.toString());   
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
