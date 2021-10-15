package orderSystem;
import java.util.ArrayList;

public class Order {
 String orderID;
 String trackingNumber;
 String orderTime;
 ArrayList<Item> orderList = new ArrayList<Item>();
  
  //Constructor takes in 4 parameters
  public Order(String orderID, String trackingNumber, String orderTime, ArrayList<Item> orderList){
    this.orderID = orderID;
    this.trackingNumber = trackingNumber;
    this.orderItem = orderItem;
    this.orderList = orderList
  }
  
  //ability to edit an existing order by either adding/removing from the existing quantity. 
  //updates the price after
  public boolean editOrder(){...}
  
  //updates the price of an item 
  public boolean updatePrice(Item item){...}
  
  //sends the order to the kitchen to be processed
  public boolean sendOrder(){...}
  
  //prints the order for the user to have a record of 
  public void printReceipt(){...}
  
  //removes an existing item from the menu
  public boolean removeItem(){...}
  
  //gets a quantity of an item and adds it to the order
  public void getQuantity(Item item, int quantity){...}
  
  //adds an item to the order
  public boolean addItem(Item item){...}
}
  
