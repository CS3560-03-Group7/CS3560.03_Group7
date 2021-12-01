package cs3560.kiosk.project;

public class Item {
    
    private int itemID;
    private String itemName;
    private String category;
    private int isAvailable;
    private double price;
    private String picture;
    
    public Item() {
        this.itemID = 0;
        this.itemName = "";
        this.category = "";
        this.isAvailable = 0;
        this.price = 0;
        this.picture = "";
    }
    
    public Item(int itemID, String itemName, String category, int isAvailable, double price, String picture) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = category;
        this.isAvailable = isAvailable;
        this.price = price;
        this.picture = picture;
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
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
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
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
}