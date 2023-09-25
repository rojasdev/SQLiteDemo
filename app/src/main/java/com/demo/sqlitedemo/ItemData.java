package com.demo.sqlitedemo;

public class ItemData {
        String ItemName;
        String ItemDescription;
        String ItemImage;
        int id;

        public ItemData(int id, String itemName, String itemDescription, String itemImage) {
            this.id = id;
            this.ItemName = itemName;
            this.ItemDescription = itemDescription;
            this.ItemImage = itemImage;
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
}
