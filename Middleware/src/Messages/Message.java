/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matthew
 */
import Agents.MetaAgent;
import Messages.MessageType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Message {
    /**
     *Stores the message to be sent to the recipient
     */
    private String messageText;
    /**
     *Stores the messages ID number
     */
    private int id;
    /**
     *Stores the name of the sender
     */
    private MetaAgent sender;
    /**
     *Stores the previous portal that forwarded the message
     */

    private MetaAgent recipient;
    /**
     *Stores the date the message was sent
     */
    private Calendar date = new GregorianCalendar();

    MessageType typeOfMessage;
    
    public Message(MessageType type, MetaAgent sender, MetaAgent recipient){
        this.sender = sender;
        this.recipient = recipient;
	this.typeOfMessage = type;
        date = Calendar.getInstance();
        
        /**
        *Temp generation for the ID
        */
        Random random = new Random();
        id = random.nextInt(100000);
    }
    
    /**
    *Returns the date as a string
    * @return 
    */
    public String dateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        return dateFormat.format(date.getTime());
    }
    

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MetaAgent getSender() {
        return sender;
    }


    public MetaAgent getRecipient() {
        return recipient;
    }

    public Calendar getDate() {
        return date;
    }  
}