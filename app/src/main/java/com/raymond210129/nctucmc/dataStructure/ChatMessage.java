package com.raymond210129.nctucmc.dataStructure;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messaageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messaageUser) {
        this.messageText = messageText;
        this.messaageUser = messaageUser;
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText() {
        this.messageText = messageText;
    }

    public String getMessaageUser() {
        return messaageUser;
    }

    public void setMessaageUser() {
        this.messaageUser = messaageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public  void setMessageTime() {
        this.messageTime = messageTime;
    }


}
