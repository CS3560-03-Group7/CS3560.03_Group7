package orderSystem;

public class Item {
String itemID;
String itemName;
boolean isAvailable;
double price;
String pictureID;
int quantity;
  
  //Constructor, takes in 6 parameters; don't need to overload constructor
  public Item(String itemID, String itemName, boolean isAvailable, double price, String pictureID, int quantity){
    this.itemID = itemID;
    this.itemName = itemName;
    this.isAvailable = isAvailable;
    this.price = price;
    this.pictureID = pictureID;
    this.quantity = quantity;
  }
  
}
