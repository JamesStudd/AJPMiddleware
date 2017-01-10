/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

import Agents.MetaAgent;
import java.util.HashMap;

/**
 *
 * @author chris
 */
public enum MessageType {

	PASS_MESSAGE(String.class, "Message being passed"),
	ADD_NODE(MetaAgent.class, "Node being added"),
	ERROR(String.class, "Error message"),
	UPDATE_ADDRESSES(HashMap.class, "Updating addresses"),
	ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY(Message.class, "Address not found, adding to lost property"),
	FAILED_TO_DELIVER(Message.class, "Failed to deliver"),
	WRONG_TYPE_OF_OBJECT_WAS_SENT_WITH_THIS_MESSAGE(Message.class, "Wrong type of object was sent with this message"),
	ADD_NODE_MONITOR(MetaAgent.class, "Adding node monitor ");

	Class requiredType;
	String description;

	private MessageType(Class t,String description) {
		requiredType = t;
		this.description = description;

	}

	public boolean checkType(Object o) {
		return o.getClass() == requiredType || o.getClass().getSuperclass() == requiredType;
	}

	public String toString(){
		return description;
		
	}
}
