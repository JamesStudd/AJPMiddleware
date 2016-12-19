/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matthew
 */
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
    private String sender;
    /**
     *Stores the previous portal that forwarded the message
     */
    private String previousPortal;
    /**
     *Stores the messages intended recipient
     */
    private String recipient;
    /**
     *Stores the date the message was sent
     */
    private Calendar date = new GregorianCalendar();
    
    public Message(String messageText, String sender, String recipient){
        this.messageText = messageText;
        this.sender = sender;
        this.recipient = recipient;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setPreviousPortal(String previousPortal) {
        this.previousPortal = previousPortal;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getPreviousPortal() {
        return previousPortal;
    }

    public String getRecipient() {
        return recipient;
    }

    public Calendar getDate() {
        return date;
    }  
}


