/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs3560.pkgfinal.project;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;

public class Item {
    private int itemID;
    private String category;
    private String itemName;
    private int isAvailable;
    private double price;
    private String pictureID;
    private int quantity;
    private int hasSizes;

    //Constructor takes in itemID and populates instance of item based on what exists in the database
    //for that itemID
    public Item(int itemID) throws SQLException{
        this.itemID = itemID;
       String dbURL = "jdbc:mysql://localhost:3306/cs3560f21";
        String user = "root";
        String pass = "root";
        SQLConnector s = new SQLConnector(dbURL, user, pass);

        String query = "SELECT * FROM item WHERE itemID =" + Integer.toString(this.itemID) + ";";
        CachedRowSet results = s.query(query);

        //need to set picture ID for this bad boy
        if(results.next()) {
            this.category = results.getString("Category");
            this.itemName = results.getString("itemName");
            this.isAvailable = results.getInt("isAvailable");
            this.price = results.getDouble("price");
            this.pictureID = results.getString("picture");
            this.quantity = 0;
        }
        else {
            this.itemID = -1;
            this.category = "";
            this.itemName = "";
            this.isAvailable = -1;
            this.price = -1;
            this.pictureID = "dinoNuggiesAreCool.jpg";
            this.quantity = 0;
        }
    }

    public Item(int itemID, String itemName, String category, int hasSizes, int isAvailable, double price, String picture) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = category;
        this.hasSizes = hasSizes;
        this.isAvailable = isAvailable;
        this.price = price;
        this.pictureID = picture;
    }

    public String getCategory(){
        return  this.category;
    }
    
    public int getHasSizes(){
        return this.hasSizes;
    }

    public String getItemName() {
        return this.itemName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemID() {
        return this.itemID;
    }

    public String getPictureID() {
        return this.pictureID;
    }

    public void setCategory(String newCategory) {
        if(newCategory == "E" ||newCategory == "C" ||newCategory == "D" ||newCategory == "S")
            this.category = newCategory;
    }
    
    public void setHasSizes(int hasSizes) {
        this.hasSizes = hasSizes;
    }

    public void setItemName(String newName) {
        this.itemName = newName;
    }

    public void setAvailability(int availability) {
        if(availability == 0 || availability == 1)
            this.isAvailable = availability;
    }

    public void setPrice(double newPrice) {
        if(price > 0)
            this.price = newPrice;
    }

    public void setPictureID(String newPictureID) {
        this.pictureID = newPictureID;
    }

    protected VBox getMenuItem(){
        //setting up picture
        Image foodPic = new Image(getClass().getResource(pictureID).toExternalForm());
        ImageView pic = new ImageView(foodPic);
        pic.setFitWidth(150);
        pic.setFitHeight(150);

        //label for food
        Label itemLabel = new Label(itemName);
        itemLabel.getStyleClass().add("label-Items");
        //label for price
        String price = String.format("$%.2f", this.price);
        Label itemPrice = new Label(price);
        itemPrice.getStyleClass().add("label-Items");
        //labels to be placed under the picture
        HBox labels = new HBox(itemLabel, itemPrice);
        labels.setAlignment(Pos.CENTER);
        labels.setSpacing(10);

        VBox itemOnMenu = new VBox(pic, labels);
        itemOnMenu.setAlignment(Pos.CENTER);
        itemOnMenu.setSpacing(5);

        return itemOnMenu;
    }

    //pretty much gives a new scene for when an item is clicked for center pane
    //to be implemented
    VBox onItemClicked(){
        Image itemImage = new Image(getClass().getResource(this.pictureID).toExternalForm());
        ImageView item = new ImageView(itemImage);
        item.setFitWidth(250);
        item.setPreserveRatio(true);

        Label iName = new Label(this.itemName);
        iName.getStyleClass().add("label-Subtitle");

        String pricey = String.format("%.2f", this.price);
        Label iPrice = new Label(pricey);
        iPrice.getStyleClass().add("label-Items");
        Label quantityLabel = new Label("0");

        //goes back to main menu
        Button cancel = new Button("cancel");
        //need to fix cancel button to return to previous page... no clue need to set up menu
        cancel.setOnAction(actionEvent -> {
            return;
        });
        cancel.getStyleClass().add("button-cancel");
        Button add = new Button("+");
        add.setOnAction(actionEvent -> {
            quantity++;
            quantityLabel.setText(String.valueOf(quantity));
        });
        Button subtract = new Button("-");
        subtract.setOnAction(actionEvent -> {
            if(quantity==0)
                quantityLabel.setText(String.valueOf(0));
            else{
                quantity--;
                quantityLabel.setText(String.valueOf(quantity));
            }
        });
        quantityLabel.getStyleClass().add("label-Items");
        Button addItemToCart = new Button();
        addItemToCart.setOnAction(actionEvent -> {
            if(quantity == 0){
                System.out.println("Nothing added to cart; Select quantity");
            }
            else {
                System.out.println(itemName + " x" + quantity + " has been added to cart");
            }
        });

        HBox cancelTop = new HBox(cancel);
        cancelTop.setAlignment(Pos.TOP_LEFT);
        HBox itemTitle = new HBox(iName);
        itemTitle.setAlignment(Pos.CENTER);
        VBox topTitle = new VBox(cancelTop, itemTitle);
        topTitle.setAlignment(Pos.CENTER);
        topTitle.setSpacing(5);
        HBox quantityControl = new HBox(subtract, quantityLabel, add);
        quantityControl.setAlignment(Pos.CENTER);
        quantityControl.setSpacing(10);
        HBox addingToCart = new HBox(addItemToCart);
        VBox itemPane = new VBox(topTitle, item, quantityControl, iPrice, addingToCart);
        itemPane.setAlignment(Pos.CENTER);
        System.out.println(this.itemName+" was clicked");
        return itemPane;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decrementQuantity() {
        quantity--;
    }
    public int getIsAvailable() {
        return isAvailable;
    }
}