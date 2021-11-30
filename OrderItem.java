/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.order.main;

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
/**
 *
 * @author Josh
 */
public class OrderItem {
    private int itemID;
    private int cartID;
    private int quantity;
    private String itemName;
    private double price;
    private String size;
    
    public OrderItem(int itemID, int quantity, SQLConnector s) throws SQLException {
        this.itemID = itemID;
        this.quantity = quantity;
        this.size = "";
        String query = "SELECT * FROM item WHERE itemID = " + Integer.toString(this.itemID);
        
        CachedRowSet results = s.query(query);
        results.next();
        this.itemName = results.getString("itemname");
        this.price = results.getDouble("price") * this.quantity;
        
        results = s.query("SELECT cartID FROM cart WHERE cartID=(SELECT max(cartID) FROM cart);");
        results.next();
        this.cartID = results.getInt("cartID");
    }
    
    public OrderItem(int itemID, int quantity, String size, SQLConnector s) throws SQLException {
        this.itemID = itemID;
        this.quantity = quantity;
        this.size = size;
        String query = "SELECT * FROM item WHERE itemID = " + Integer.toString(this.itemID);
        
        CachedRowSet results = s.query(query);
        results.next();
        String tempName = results.getString("itemname");
        double tempPrice = results.getDouble("price");//base item price
        switch(this.size){
            case "Small": case "small": case "S": case "s":
                tempName = "Small " + tempName;
                tempPrice = tempPrice - 1;
                break;
            case "Medium": case "medium": case "M": case "m":
                tempName = "Medium " + tempName;
                break;
            case "Large": case "large": case "L": case "l":
                tempName = "Large " + tempName;
                tempPrice = tempPrice + 1;
                break;
        }
        
        this.itemName = tempName;
        this.price = tempPrice * this.quantity;
        
        results = s.query("SELECT cartID FROM cart WHERE cartID=(SELECT max(cartID) FROM cart);");
        results.next();
        this.cartID = results.getInt("cartID");
    }
    
    public int getItemID(){
        return this.itemID;
    }
    
    public int getCartID(){
        return this.cartID;
    }
    
    public int getQuantity(){
        return this.quantity;
    }
    
    public String getItemName(){
        return this.itemName;
    }
    
    public double getPrice(){
        return this.price;
    }
    
}
