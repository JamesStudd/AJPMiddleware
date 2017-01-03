/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;

/**
 *
 * @author chris
 */
public class NodeMonitor extends MetaAgent{
	
	public NodeMonitor(String name) {
		super(name);
	}

	@Override
	void handle(Message message) {
		System.out.println("I need finishing");
                System.out.println("Test");
		
	}
	
}
