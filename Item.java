package com.example.ordertime;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;

class Item{
    private String itemID;
    private String itemName;
    boolean isAvailable;
    private double price;
    private String pictureID;
    private int quantity;

    //Constructor, takes in 6 parameters; don't need to overload constructor
    public Item(String itemID, String itemName, double price, String pictureID, int quantity){
        this.itemID = itemID;
        this.itemName = itemName;
        this.isAvailable = true;
        this.price = price;
        this.pictureID = pictureID;
        this.quantity = 0;
    }

    public Item(){
        this("", "", 0.0, "", 0);
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public String getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity(){
        quantity++;
    }

    public void decrementQuantity(){
        quantity--;
    }

}


