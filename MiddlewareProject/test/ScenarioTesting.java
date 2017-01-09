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
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
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
           
            
            assertEquals(T1P2.getRegisteredAddresses(), P2Test);
            assertEquals(T1P3.getRegisteredAddresses(), P3Test);
            assertEquals(T1P4.getRegisteredAddresses(), P4Test);
            
 
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
