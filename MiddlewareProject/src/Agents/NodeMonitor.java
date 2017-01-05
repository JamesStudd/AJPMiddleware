/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import GUI.MessageBoard;

/**
 *
 * @author chris
 */
public class NodeMonitor extends MetaAgent {

    private MessageBoard mb;

    public NodeMonitor(String name) {
        super(name);
        mb = new MessageBoard();
    }

    @Override
    void handle(Message message) {
        String obj, type, date, sender, recip;

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
                
            default:
                obj = "";
                type = "";

        }

        sender = message.getSender(); // Already strings
        recip = message.getRecipient();
        date = message.dateString();

        mb.receivedNewMessage(obj, type, date, sender, recip);
    }

}
