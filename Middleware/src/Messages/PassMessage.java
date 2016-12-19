/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;
import Messages.Message;

import Agents.MetaAgent;

/**
 *
 * @author chris
 */
public class PassMessage extends Message {


	static final MessageType type = MessageType.USER_TO_USER;
	private  String messageToBePassed;
	
	public PassMessage(MetaAgent sender, MetaAgent reciptient, String message) {
		super(type, sender, reciptient);
		this.messageToBePassed = message;
	}


	
}
