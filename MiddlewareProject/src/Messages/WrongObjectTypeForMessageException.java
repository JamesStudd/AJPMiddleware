/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 * An error flagged when a message is created with the wrong type 
 */
public class WrongObjectTypeForMessageException extends Exception {

	/**
	 * Creates a new instance of
	 * <code>wrongObjectTypeForMessageException</code> without detail
	 * message.
	 */
	public WrongObjectTypeForMessageException() {
	}

	/**
	 * Constructs an instance of
	 * <code>wrongObjectTypeForMessageException</code> with the specified
	 * detail message.
	 *
	 * @param msg the detail message.
	 */
	public WrongObjectTypeForMessageException(String msg) {
		super(msg);
	}
}
