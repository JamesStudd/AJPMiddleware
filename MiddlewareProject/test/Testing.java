/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.MetaAgent;
import Agents.Portal;
import Agents.UserAgent;
import java.util.HashMap;
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
public class Testing {   
    
    //@Test
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
            
            T1P5.showAddresses();
            
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
            
            HashMap<String, MetaAgent> P3Test = new HashMap<String, MetaAgent>();
            P3Test.put("HeadPortal", HeadPortal);
            P3Test.put("T1P5", T1P5);
            P3Test.put("T1P2Two", HeadPortal);
            P3Test.put("T1P3", T1P3);
            P3Test.put("T1P4", HeadPortal);
            P3Test.put("T1P2", HeadPortal);
            P3Test.put("T1P4One", HeadPortal);
            P3Test.put("T1P5One", T1P5);
            P3Test.put("T1P3One", T1P3One);
            
            HashMap<String, MetaAgent> P4Test = new HashMap<String, MetaAgent>();
            P4Test.put("T1P5", T1P2);
            P4Test.put("T1P2Two" ,T1P2);
            P4Test.put("T1P3", T1P2);
            P4Test.put("T1P4One", T1P4One);
            P4Test.put("T1P5One", T1P2);
            P4Test.put("T1P3One", T1P2);
            P4Test.put("T1P2One", T1P2);
            P4Test.put("HeadPortal", T1P2);
            P4Test.put("T1P2", T1P2);
            
            HashMap<String, MetaAgent> P5Test = new HashMap<String, MetaAgent>();
            P5Test.put("T1P2Two", T1P3);
            P5Test.put("T1P3", T1P3);
            P5Test.put("T1P4", T1P3);
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
    //Need to figure out how to assertEquals on some tests
    //I guess just do a version of this with 2 tiers of portals and send a message up 1 layer when it's out of scope and show it doesn't work
    //@Test
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
    
    //@Test
    //This seems way too simple of a test?
    public void portalPortalLink() throws InterruptedException {
        Portal tier1Portal = new Portal("tier1Portal");
        Portal tier2Portal = new Portal("tier2Portal", tier1Portal);
        
        Thread.sleep(2000);
        
        tier1Portal.showAddresses();
    }
    
    //repeating tests to validate scoping?
    //create a portal that is scoped elswhere and show it updates to scope
    //Make a portal on Tier 2 and show that the reference on Tier 3 points to Tier 2
    //SINGLE HIERARCHIES NOT MULTIPLE
    @Test
    public void agentRegistration() throws InterruptedException {
            //Head
            Portal HeadPortal = new Portal("HeadPortal");
            //Tier 2
            Portal T1P2 = new Portal("T1P2", HeadPortal);
	    
            //Tier 3
            Portal T1P3 = new Portal("T1P3", T1P2);
            
            System.out.println("\n\n");
                     
           UserAgent T1P2Test = new UserAgent("T1P2Test", T1P2, null);
           
           Thread.sleep(1000);
           
           T1P3.showAddresses();
    }
    
    
    
    
    
    
    
    }
