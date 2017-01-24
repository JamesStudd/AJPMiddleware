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
            
            //Master portal
            Portal headPortal = new Portal("headPortal");
            //First tier, seperating the tree
            Portal tier1Left = new Portal("tier1Left", headPortal);
            Portal tier1Right = new Portal("tier1Right", headPortal);

            //Generating portals for left side of tree
            Portal leftPortal1 = new Portal("leftPortal1", tier1Left);
            Portal leftPortal2 = new Portal("leftPortal2", tier1Left);
            Portal leftPortal3 = new Portal("leftPortal3", tier1Left);
            Portal leftPortal4 = new Portal("leftPortal4", tier1Left);
            
            //Generating portals for right side of tree
            Portal rightPortal1 = new Portal("rightPortal1", tier1Right);
            Portal rightPortal2 = new Portal("rightPortal2", tier1Right);
            Portal rightPortal3 = new Portal("rightPortal3", tier1Right);
            Portal rightPortal4 = new Portal("rightPortal4", tier1Right);
            
            //Creating user agents for left side of tree:
            
            //Tier 1 Left:
            UserAgent tier1LeftAgent1 = new UserAgent("tier1LeftAgent1", tier1Left, null);
            UserAgent tier1LeftAgent2 = new UserAgent("tier1LeftAgent2", tier1Left, null);
            
            //Generating user agents for the children portals (left side)
            UserAgent leftPortal1Agent1 = new UserAgent("leftPortal1Agent1", leftPortal1, null);
            UserAgent leftPortal1Agent2 = new UserAgent("leftPortal1Agent2", leftPortal1, null);
            UserAgent leftPortal1Agent3 = new UserAgent("leftPortal1Agent3", leftPortal1, null);
            UserAgent leftPortal1Agent4 = new UserAgent("leftPortal1Agent4", leftPortal1, null);
            
            UserAgent leftPortal2Agent1 = new UserAgent("leftPortal2Agent1", leftPortal2, null);
            UserAgent leftPortal2Agent2 = new UserAgent("leftPortal2Agent2", leftPortal2, null);
            UserAgent leftPortal2Agent3 = new UserAgent("leftPortal2Agent1", leftPortal3, null);
            UserAgent leftPortal2Agent4 = new UserAgent("leftPortal2Agent1", leftPortal4, null);

            UserAgent leftPortal3Agent1 = new UserAgent("leftPortal3Agent1", leftPortal3, null);
            UserAgent leftPortal3Agent2 = new UserAgent("leftPortal3Agent2", leftPortal3, null);
            UserAgent leftPortal3Agent3 = new UserAgent("leftPortal3Agent3", leftPortal3, null);
            UserAgent leftPortal3Agent4 = new UserAgent("leftPortal3Agent4", leftPortal3, null);

            UserAgent leftPortal4Agent1 = new UserAgent("leftPortal4Agent1", leftPortal4, null);
            UserAgent leftPortal4Agent2 = new UserAgent("leftPortal4Agent2", leftPortal4, null);
            UserAgent leftPortal4Agent3 = new UserAgent("leftPortal4Agent3", leftPortal4, null);
            UserAgent leftPortal4Agent4 = new UserAgent("leftPortal4Agent4", leftPortal4, null);
            
            //Creating user agents for right side of tree:
            
            //Tier 1 Right:
            UserAgent tier1RightAgent1 = new UserAgent("tier1RightAgent1", tier1Right, null);
            UserAgent tier1RightAgent2 = new UserAgent("tier1RightAgent2", tier1Right, null);
            
            //Generating user agents for the children portals (right side)
            UserAgent rightPortal1Agent1 = new UserAgent("rightPortal1Agent1", rightPortal1, null);
            UserAgent rightPortal1Agent2 = new UserAgent("rightPortal1Agent2", rightPortal1, null);
            UserAgent rightPortal1Agent3 = new UserAgent("rightPortal1Agent3", rightPortal1, null);
            UserAgent rightPortal1Agent4 = new UserAgent("rightPortal1Agent4", rightPortal1, null);
            
            UserAgent rightPortal2Agent1 = new UserAgent("rightPortal2Agent1", rightPortal2, null);
            UserAgent rightPortal2Agent2 = new UserAgent("rightPortal2Agent2", rightPortal2, null);
            UserAgent rightPortal2Agent3 = new UserAgent("rightPortal2Agent3", rightPortal2, null);
            UserAgent rightPortal2Agent4 = new UserAgent("rightPortal2Agent4", rightPortal2, null);
            
            UserAgent rightPortal3Agent1 = new UserAgent("rightPortal3Agent1", rightPortal3, null);
            UserAgent rightPortal3Agent2 = new UserAgent("rightPortal3Agent2", rightPortal3, null);
            UserAgent rightPortal3Agent3 = new UserAgent("rightPortal3Agent3", rightPortal3, null);
            UserAgent rightPortal3Agent4 = new UserAgent("rightPortal3Agent4", rightPortal3, null);
            
            UserAgent rightPortal4Agent1 = new UserAgent("rightPortal4Agent1", rightPortal4, null);
            UserAgent rightPortal4Agent2 = new UserAgent("rightPortal4Agent2", rightPortal4, null);
            UserAgent rightPortal4Agent3 = new UserAgent("rightPortal4Agent3", rightPortal4, null);
            UserAgent rightPortal4Agent4 = new UserAgent("rightPortal4Agent4", rightPortal4, null);
            
            
            
            
            
            
            
            
            
            
            
            
            
        }
        }
