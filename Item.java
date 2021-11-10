package orderSystem;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;

class Item {
    private String itemID;
    private String itemName;
//    boolean isAvailable;
    private double price;
    private String pictureID;
    private int quantity;

    //Constructor, takes in 6 parameters; don't need to overload constructor
    public Item(String itemID, String itemName, double price, String pictureID, int quantity){
        this.itemID = itemID;
        this.itemName = itemName;
//        this.isAvailable = isAvailable;
        this.price = price;
        this.pictureID = pictureID;
        this.quantity = 0;
    }

    public Item(){
        this("", "", 0.0, "", 0);
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public String getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

}


