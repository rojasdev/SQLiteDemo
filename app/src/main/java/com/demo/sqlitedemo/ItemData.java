package com.demo.sqlitedemo;

public class ItemData {
        String ItemName;
        String ItemDescription;
        String ItemImage;

        String ItemDate;

        String ItemTime;

        int ItemQuantity;
        int id;

        public ItemData(int id, String itemName, String itemDescription, String itemImage) {
            this.id = id;
            this.ItemName = itemName;
            this.ItemDescription = itemDescription;
            this.ItemImage = itemImage;
        }

    public ItemData(String itemName, String itemDescription, String itemImage, String itemDate, String itemTime, int itemQuantity) {
        ItemName = itemName;
        ItemDescription = itemDescription;
        ItemImage = itemImage;
        ItemDate = itemDate;
        ItemTime = itemTime;
        ItemQuantity = itemQuantity;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public int getItemId() {
        return id;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemDate() {
        return ItemDate;
    }

    public void setItemDate(String itemDate) {
        ItemDate = itemDate;
    }

    public String getItemTime() {
        return ItemTime;
    }

    public void setItemTime(String itemTime) {
        ItemTime = itemTime;
    }

    public int getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
