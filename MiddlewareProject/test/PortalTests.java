/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.Portal;
import Agents.UserAgent;
import Agents.MetaAgent;
import Messages.Message;
import Messages.MessageType;
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
public class PortalTests {
    
    @Test
    public void constructorNoParent() { 
        Portal testPortal = new Portal("testPortal");
        String expected = "testPortal";
        String actual = testPortal.toString();
        assertEquals(expected, actual);   
    }
    
    @Test
    public void constructorWithParent() { 
        Portal testPortal1 = new Portal("testPortal");
        Portal testPortal2 = new Portal("testPortal2", testPortal1);
        String expectedName = "testPortal2";
        String actualName = testPortal2.toString();
        MetaAgent expectedScope = testPortal1;
        MetaAgent actualScope = testPortal2.getScope();
        assertEquals(expectedName, actualName);   
    }
    
    @Test
    public void setParent() { 
        Portal testPortal1 = new Portal("testPortal");
        Portal testPortal2 = new Portal("testPortal2", testPortal1);
        Portal testPortal3 = new Portal("testPortal3");
        
        testPortal2.setParent(testPortal3);
        
        MetaAgent expected = testPortal3;
        MetaAgent actual = testPortal2.getParent();

        assertEquals(expected, actual);   
    }
    
    @Test
    public void isForMe() { 
        Portal testPortal1 = new Portal("testPortal");
        Portal testPortal2 = new Portal("testPorta2", testPortal1);
        UserAgent P1Agent = new UserAgent("P1Agent", testPortal1, null);
        UserAgent P2Agent = new UserAgent("P2Agent", testPortal2, null);
        
        P2Agent.passOverAMessage("P1Agent", "Test Message");
        

        boolean actual = testPortal1.isForMe(message.getRecipient);
    }
    
    
    
    
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
