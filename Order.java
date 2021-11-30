package com.order.main;

public class Order
{
    private int trackingNum; //this tracking number will be set by the Cart class
    private ArrayList<OrderItem> orderList = new ArrayList<OrderItem>(); //this arraylist will be attached to the cart to track the order

    public Order()
    {
        trackingNum = 0;
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
}