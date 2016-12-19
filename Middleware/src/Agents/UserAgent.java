/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import javax.management.monitor.Monitor;

/**
 *
 * @author Sean
 */
public class UserAgent extends MetaAgent {
        
    
        public UserAgent(MetaAgent scope) {
            super(name, scope);
        }
    
        private MetaAgent scope;

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//Should be in the meta
	private Thread thread = new Thread();
	
	//Public enabling others to add messages to its queue
	public void addToQueue(Message message){
		queue.add(message);
	}
        
        //public void newMessage (all params needed for message) {};
        
        //public void sendMessage (Message message)();

	//Handles a message pull
	private void handle(Message message){
		updateMonitors(message);		
	}
        
        //A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();

	@Override
	public void run() {

		while(true){
			handle(queue.remove());
			
		}
	}


}

    

    
}
