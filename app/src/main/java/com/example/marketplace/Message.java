package com.example.marketplace;

public class Message {

    private String sellerId;
    private String buyerId;
    private String messages;
    private String messageId;
    private String itemId;


    public Message()
    {

    }
    public Message(String sellerId, String buyerId, String messages, String messageId, String itemid) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.messages = messages;
        this.messageId = messageId;
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
