/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

import Agents.MetaAgent;
import java.util.HashMap;

/**
 * This enum contains all the avaliable message types and the class of object 
 * they should contain
 *
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

	/**
	 * Constructor sets the object variables
	 * @param messageObjectClass - The class of the object held within this type of message
	 * @param description  - The description of the message
	 */
	private MessageType(Class messageObjectClass,String description) {
		requiredType = messageObjectClass;
		this.description = description;

	}

	/**
	 * Checks that the object passed in is of the same type or a decendent 
	 * of the type required
	 * @param o - The object to be checked against
	 * @return  True if is of the correct type
	 */
	public boolean checkType(Object o) {
		return o.getClass() == requiredType || o.getClass().getSuperclass() == requiredType;
	}


	/**
	 * Get the description
	 * @return  - The description of this message type
	 */
	@Override
	public String toString(){
		return description;
		
	}
}
