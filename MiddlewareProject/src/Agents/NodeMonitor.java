/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import GUI.MessageBoard;
import Messages.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author chris
 */
public class NodeMonitor extends MetaAgent {

    private MessageBoard mb;

    ArrayList<MetaAgent> nodesWatching = new ArrayList();
    HashMap<String, ArrayList<Message>> watching = new HashMap();
    HashMap<String, String> nodeHistoy = new HashMap<>();

    

    public NodeMonitor(String name) {
        super(name);
        mb = new MessageBoard(this);
        mb.setVisible(true);
    }

    public String formatHistory(String obj ,String type, String date, String sender, String recip, String history){

	    return history + String.format("-----------------------\n"
		    + " Date %s \n Type %s \n Sender %s \n Recpient  %s \n Message %s "
		    + " \n -----------------------\n\n", date, type, sender, recip, obj);
	    
    }
    
    public void changeNodeTo(String node){

	    System.out.println("in here " + node);
	    ArrayList<Message> nodesList = watching.get(node);
	    System.out.println(nodesList);
	    if(nodesList != null && !nodesList.isEmpty()){

	   Message lastMessage = nodesList.get(nodesList.size()-1);

	    handle(lastMessage);
	    }
    }

    @Override
    void handle(Message message) {

	
	String obj = "";
	String type = "";
	String date = message.getDate().toString();
	String sender = "";
	String recip = "";

	if(watching.containsKey(message.getRecipient())){
		watching.get(message.getRecipient()).add(message);
	}

        switch (message.getMessageType()) {
            case PASS_MESSAGE:
                obj = (String) message.retrieveMessageItem();
                type = "String";
                break;

            case ERROR:
                obj = (String) message.retrieveMessageItem();
                type = "String";
                break;

            case ADD_NODE:
                MetaAgent n = (MetaAgent) message.retrieveMessageItem();
                obj = n.toString() + " node added with scope: " + n.getScope();
                type = "MetaAgent";
                break;
                // use toString and getScope
                
            case UPDATE_ADDRESSES:
                obj = "Address Updated";
                type = "HashMap";
                break;
                // 
                
            case ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY:
                obj = (String) message.retrieveMessageItem();
                type = "Message";
                break;

            case FAILED_TO_DELIVER:
                obj = (String) message.retrieveMessageItem();
                type = "Message";
                break;

            case WRONG_TYPE_OF_OBJECT_WAS_SENT_WITH_THIS_MESSAGE:
                obj = (String) message.retrieveMessageItem();
                type = "Message";
                break;
	    case ADD_NODE_MONITOR:
		    MetaAgent agent = (MetaAgent) message.retrieveMessageItem();
		    agent.addToQueue(new Message(MessageType.ADD_NODE_MONITOR, this.toString(), agent.toString(), this));
		    nodesWatching.add(agent);
		    watching.put(agent.toString(), new ArrayList());
		    return;
                
            default:
                obj = "";
                type = "";

        }

        sender = message.getSender(); // Already strings
        recip = message.getRecipient();
        date = message.dateString();

	
	nodeHistoy.put(recip, formatHistory(obj, type, date, sender, recip, nodeHistoy.get(recip)));

        mb.receivedNewMessage(obj, type, date, sender, recip, nodeHistoy.get(recip));
    }


}
