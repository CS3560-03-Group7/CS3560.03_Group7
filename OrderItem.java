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
    
    public OrderItem(int itemID, int quantity, SQLConnector s) throws SQLException {
        this.itemID = itemID;
        this.quantity = quantity;
        
        String query = "SELECT * FROM item WHERE itemID = " + Integer.toString(this.itemID);
        
        CachedRowSet results = s.query(query);
        results.next();
        this.itemName = results.getString("itemname");
        this.price = results.getDouble("price") * this.quantity;
        
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
