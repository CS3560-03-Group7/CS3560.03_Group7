package main;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

public class Order
{
    private int orderID;
    private int orderCartID;
    private int trackingNum; //this tracking number will be set by the Cart class
    private ArrayList<OrderItem> orderList = new ArrayList<OrderItem>(); //this arraylist will be attached to the cart to track the order
    private LocalDateTime orderTime;

    public Order(SQLConnector s, int cartID, int trackingNum, LocalDateTime orderTime)
    {
        this.orderCartID = cartID;
        this.trackingNum = trackingNum;
        this.orderTime = orderTime;
        
        String query = "SELECT orderID FROM `order` WHERE orderCartID = " + Integer.toString(this.orderCartID) + ";";
        try {
            CachedRowSet results = s.query(query);
            results.next();
            this.orderID = results.getInt("orderID");
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //standard accessor/mutator methods for trackingNum
    public int getTrackingNum()
    {
        return trackingNum;
    }

    public void setTrackingNum(int trackingNum)
    {
        this.trackingNum = trackingNum;
    }

    //setter that will attach the cart and all of its items to the order
    public void setOrderList(ArrayList<OrderItem> cartList)
    {
        orderList = cartList;
    }
    
    public void sendOrderToKitchen(){
        System.out.println("Order #" + this.trackingNum + " sent to kitchen.");
    }
}