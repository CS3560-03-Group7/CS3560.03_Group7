package cs3560.kiosk.project;

public class Product {
    
    private int itemID;
    private String itemName;
    private int isAvailable;
    private double price;
    
    public Product() {
        this.itemID = 0;
        this.itemName = "";
        this.isAvailable = 0;
        this.price = 0;
    }
    
    public Product(int itemID, String itemName, int isAvailable, double price) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.isAvailable = isAvailable;
        this.price = price;
    }
    
    public int getItemID() {
        return itemID;
    }
    
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public int getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}