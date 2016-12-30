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

	PASS_MESSAGE(String.class), 
	ADD_NODE(MetaAgent.class), 
	ERROR(String.class), 
	UPDATE_ADDRESSES(HashMap.class), 
	ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY(Message.class), 
	FAILED_TO_DELIVER(Message.class), 
	WRONG_TYPE_OF_OBJECT_WAS_SENT_WITH_THIS_MESSAGE(Message.class); 

	Class requiredType;

	private MessageType(Class t) {
		requiredType = t;
	}

	public boolean checkType(Object o){
		return o.getClass()== requiredType || o.getClass().getSuperclass() == requiredType;
	}
}

	