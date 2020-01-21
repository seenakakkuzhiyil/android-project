package com.example.marketplace;

public class Product {

    private String ItemId, userId, itemName, itemDescription;
    private String itemCategory;
    private String itemUploadDate, itemUploadTime;
    private String itemPhoto;
    private String itemPrice;

    public Product() {
    }

    public Product(String itemId, String userId, String itemName, String itemCategory, String itemDescription, String itemPrice, String itemUploadDate, String itemUploadTime, String itemPhoto) {
        ItemId = itemId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemUploadDate = itemUploadDate;
        this.itemUploadTime = itemUploadTime;
        this.itemPhoto = itemPhoto;
        this.itemCategory = itemCategory;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemUploadDate() {
        return itemUploadDate;
    }

    public void setItemUploadDate(String itemUploadDate) {
        this.itemUploadDate = itemUploadDate;
    }

    public String getItemUploadTime() {
        return itemUploadTime;
    }

    public void setItemUploadTime(String itemUploadTime) {
        this.itemUploadTime = itemUploadTime;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public void setItemPhoto(String itemPhoto) {
        this.itemPhoto = itemPhoto;
    }
}
