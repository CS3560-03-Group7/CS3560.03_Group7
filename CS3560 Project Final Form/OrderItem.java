/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

//This class constructs OrderItems for display in the shopping cart by reading information from the orderitem and item tables in the database
 
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
        
        //fetching cartID
        CachedRowSet results = s.query("SELECT cartID FROM cart WHERE cartID=(SELECT max(cartID) FROM cart);");
        results.next();        
        this.cartID = results.getInt("cartID");
        
        //fetching size information, if any
        String query = "SELECT itemSize FROM orderItem WHERE cartID = " + this.cartID + ";";        
        results = s.query(query);
        results.next();        
        this.size = results.getString("itemSize");
        
        //fetching item information
        query = "SELECT * FROM item WHERE itemID = " + Integer.toString(this.itemID);
        
        results = s.query(query);
        results.next();
        String tempName = results.getString("itemname");
        double tempPrice = results.getDouble("price");//base item price
        if(this.size != null){
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
        }}
        
        this.itemName = tempName;
        this.price = tempPrice * this.quantity;
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
