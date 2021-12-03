package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.sql.rowset.CachedRowSet;

public class MainMenu extends Application {
    private Image dinoNugs;
    private Image burger;
    private Image fries;
    private Image kidsMeal;
    private Image milkShake;
    private Image rootBeerFloat;
    private Image burgerCombo;
    private Image megaBurger;
    private Image banner;

    private ImageView nugView;
    private ImageView burgerView;
    private ImageView friesView;
    private ImageView kidsMealView;
    private ImageView milkShakeView;
    private ImageView rootbeerFloatView;
    private ImageView bannerView;
    private int quantity = 1; //this is used in the addToOrder function
    private Stage popupwindow = new Stage(); //so is this
    private String size; //aaaand so is this
    private SQLConnector s;
    private String filepath = "C:\\Users\\Josh\\Desktop\\CS3560.03_Group7-styled-and-works\\CS3560.03_Group7-styled-and-works\\CS3560FinalProject\\src\\main\\resources\\com\\example\\cs3560finalproject\\";

    public MainMenu(SQLConnector s){
        this.s = s;
    }
    private void loadImages() throws FileNotFoundException {
       
        burger = new Image(new FileInputStream(filepath + "borgor.jpg"));
        burgerCombo = new Image(new FileInputStream(filepath + "burgerCombo.jpg"));
        dinoNugs = new Image(new FileInputStream(filepath + "dinoNuggiesAreCool.jpg"));
        fries = new Image(new FileInputStream(filepath + "fritasYum.jpg"));
        kidsMeal = new Image(new FileInputStream(filepath + "kidsMealOWO.jpeg"));
        milkShake = new Image(new FileInputStream(filepath + "milkyShakey.jpg"));
        rootBeerFloat = new Image(new FileInputStream(filepath + "rootbeerFloatin.jpg"));
        megaBurger = new Image(new FileInputStream(filepath + "theshameburger.jpg"));
        //adding a banner to top of the menu so we get that cute theme going
        banner = new Image(new FileInputStream(filepath + "banner1.png"));

        nugView = new ImageView(dinoNugs);
        burgerView = new ImageView(burger);
        friesView = new ImageView(fries);
        kidsMealView = new ImageView(kidsMeal);
        milkShakeView = new ImageView(milkShake);
        rootbeerFloatView = new ImageView(rootBeerFloat);
        //for the banner view
        bannerView = new ImageView(banner);
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Menu");
        stage.setResizable(true);
        stage.setScene(getHomePage(stage));
        stage.setWidth(650);
        stage.setResizable(false);
        stage.show();
    }

    public Scene getHomePage(Stage stage) throws FileNotFoundException {
        loadImages();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        Label nugLabel = new Label("Dino Nuggets -- $7.00");
        Label burgLabel = new Label("Burger -- $9.00");
        Label fryLabel = new Label("Fries -- $2.50");
        Label kMealLabel = new Label("Kids Meal w/ Chicken Nuggets -- $6.00");
        Label mShakeLabel = new Label("Milk Shake -- $4.00");
        Label rbFloatLabel = new Label("Root Beer Float -- $6.50");
        //adding label styles to really make everything come together and pop


        //for the nuggies; set to be eventually placed in a gridpane or something for future
        //Button btnAddToOrder = new Button("Add to Order");
        //btnAddToOrder.setPrefWidth(200);
        nugView.setFitWidth(150);
        nugView.setFitHeight(150);
        Button nugBtn = new Button();
        nugBtn.setGraphic(nugView);

        nugBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(4));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        nugLabel.getStyleClass().add("label-Items");
        VBox vNugs = new VBox(nugBtn, nugLabel);
        vNugs.setAlignment(Pos.CENTER);
        vNugs.setSpacing(15);

        //for the pretty patties
        burgerView.setFitWidth(150);
        burgerView.setFitHeight(150);
        Button burgerBtn = new Button();
        burgerBtn.setGraphic(burgerView);

        burgerBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(1));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        //change any .label-white to label-Items
        burgLabel.getStyleClass().add("label-Items");
        VBox vBurg = new VBox(burgerBtn, burgLabel);
        vBurg.setAlignment(Pos.CENTER);
        vBurg.setSpacing(15);

        //for the fritas
        friesView.setFitWidth(150);
        friesView.setFitHeight(150);
        Button fryBtn = new Button();
        fryBtn.setGraphic(friesView);

        fryBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(2));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        fryLabel.getStyleClass().add("label-Items");
        VBox vFry = new VBox(fryBtn, fryLabel);
        vFry.setAlignment(Pos.CENTER);
        vFry.setSpacing(15);

        //for the lunchables
        kidsMealView.setFitWidth(150);
        kidsMealView.setFitHeight(150);
        Button kidsMealBtn = new Button();
        kidsMealBtn.setGraphic(kidsMealView);

        kidsMealBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(6));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        kMealLabel.getStyleClass().add("label-Items");
        VBox vKMeal = new VBox(kidsMealBtn, kMealLabel);
        vKMeal.setAlignment(Pos.CENTER);
        vKMeal.setSpacing(15);

        //for the milkshake
        milkShakeView.setFitWidth(150);
        milkShakeView.setFitHeight(150);
        Button shakeBtn = new Button();
        shakeBtn.setGraphic(milkShakeView);

        shakeBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(3));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        mShakeLabel.getStyleClass().add("label-Items");
        VBox vShake = new VBox(shakeBtn, mShakeLabel);
        vShake.setAlignment(Pos.CENTER);
        vShake.setSpacing(15);

        //for the rootbeer floats
        rootbeerFloatView.setFitWidth(150);
        rootbeerFloatView.setFitHeight(150);
        Button floatBtn = new Button();
        floatBtn.setGraphic(rootbeerFloatView);

        floatBtn.setOnAction(e-> {
            popupwindow.setTitle("");
            popupwindow.setScene(addToOrder(8));
            popupwindow.setResizable(false);
            popupwindow.showAndWait();
        });

        rbFloatLabel.getStyleClass().add("label-Items");
        VBox vFloat = new VBox(floatBtn, rbFloatLabel);
        vFloat.setAlignment(Pos.CENTER);
        vFloat.setSpacing(15);

        //Arranging the menu items in a grid
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.CENTER);
        menu.setHgap(5);
        menu.setVgap(5);
        menu.add(vNugs, 0, 0);
        menu.add(vBurg,1,0);
        menu.add(vFry, 2, 0);
        menu.add(vKMeal,0,1);
        menu.add(vShake,1,1);
        menu.add(vFloat,2,1);

        Button cartBtn = new Button("View Cart");
        cartBtn.setOnAction(e ->{
            try {
                Cart orderCart = new Cart(s);
                stage.setTitle("Cart");
                stage.setScene(orderCart.displayCart(stage));
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        cartBtn.getStyleClass().add("button-completeOrder");

        //added in style for update button-- should be green adn hover effect is on
        Button updateBtn = new Button("Update Menu");
        updateBtn.getStyleClass().add("button-menuBtn");
        updateBtn.setOnAction(e -> {
            UpdateMenu up = new UpdateMenu(s);
            stage.setTitle("Update Menu");
            stage.setScene(up.updateMenu(stage));
            stage.setResizable(false);
        });

        VBox topContainer = new VBox();
        BorderPane buttonContainer = new BorderPane();
        buttonContainer.setLeft(updateBtn);
        buttonContainer.setRight(cartBtn);
        buttonContainer.setPadding(new Insets(0,0,15,0));


        Label restaurantName = new Label("Welcome to Burgerverse!");
        //restaurantName.setFont(new Font("Arial", 24));
        restaurantName.getStyleClass().add("label-Title");
        restaurantName.setTextAlignment(TextAlignment.CENTER);
        HBox lblContainer = new HBox(restaurantName);
        lblContainer.setAlignment(Pos.CENTER);
        //added top banner into the topContainer so it looks pretty
        topContainer.getChildren().addAll(lblContainer,bannerView,buttonContainer);
        BorderPane itemPane = new BorderPane();
        itemPane.setPadding(new Insets(20,20,20,20));
        itemPane.setTop(topContainer);
        itemPane.setAlignment(topContainer, Pos.CENTER);
        itemPane.setCenter(menu);
        Scene scene = new Scene(itemPane);
        scene.getStylesheets().add("file:///C:/Users/Josh/Desktop/styles.css");

        return scene;
    }

    private Scene addToOrder(int itemNum){
        quantity = 1;
        VBox orderBox = new VBox();
        orderBox.setSpacing(20);
        orderBox.setPadding(new Insets(10, 0, 10, 10));
        orderBox.setAlignment(Pos.CENTER);
        ToggleGroup sizes = new ToggleGroup();

        Label choices = new Label("Add to order?");
        choices.getStyleClass().add("label-Subtitle");
        RadioButton smallBtn = new RadioButton("Small");
        RadioButton medBtn = new RadioButton("Medium");
        RadioButton lrgBtn = new RadioButton("Large");
        smallBtn.setToggleGroup(sizes);
        medBtn.setToggleGroup(sizes);
        medBtn.setSelected(true);
        lrgBtn.setToggleGroup(sizes);
        HBox radioBtnBox = new HBox();

        smallBtn.setOnAction(e -> {
            size = "S";
        });
        medBtn.setOnAction(e -> {
            size = "M";
        });
        lrgBtn.setOnAction(e -> {
            size = "L";
        });
        radioBtnBox.setAlignment(Pos.CENTER);
        Button addToOrderBtn = new Button("Add to Order");

        radioBtnBox.getChildren().addAll(smallBtn, medBtn, lrgBtn);

        Button addBtn = new Button("+");
        Button removeBtn = new Button("-");
        HBox qDisplay = new HBox();
        Label quantityLbl = new Label(Integer.toString(quantity));
        quantityLbl.getStyleClass().add("label-Items");

        qDisplay.setAlignment(Pos.BASELINE_CENTER);
        qDisplay.setSpacing(10);
        qDisplay.getChildren().addAll(removeBtn,quantityLbl,addBtn);

        if(quantity <= 1)
            removeBtn.setDisable(true);

        addBtn.setOnAction(e -> {
            quantity++;
            quantityLbl.setText(Integer.toString(quantity));
            removeBtn.setDisable(false);
        });
        addBtn.getStyleClass().add("button-adding");

        removeBtn.setOnAction(e -> {
            quantity--;
            quantityLbl.setText(Integer.toString(quantity));
            if(quantity <= 1)
                removeBtn.setDisable(true);
        });
        removeBtn.getStyleClass().add("button-subtracting");

        addToOrderBtn.setOnAction(e -> {
            try {
                CachedRowSet results = s.query("SELECT cartID FROM cart WHERE cartID=(SELECT max(cartID) FROM cart);");
                results.next();
                int cart = results.getInt("cartID");

                //this query checks to see if this item already exists in the orderitem table
                String query = "SELECT * FROM orderItem WHERE cartID = " + cart + " AND itemID = " + itemNum + ";";
                results = s.query(query);

                if(itemNum == 1 || itemNum == 4 || itemNum == 7){
                    if(results.size() == 0) //if it isn't in the table, add it
                        query = "INSERT INTO orderitem(itemID,cartID,quantity) VALUES (" + itemNum + "," + cart + "," + quantity + ");";
                    else{ //if it is in the table, increase the quantity under the existing entry
                        results.next();
                        int dbQuantity = results.getInt("quantity");
                        quantity += dbQuantity;
                        query = "UPDATE orderItem SET quantity = " + quantity + " WHERE cartID = " + cart + ";";
                    }
                }
                else{
                    if(results.size() == 0)
                        query = "INSERT INTO orderitem VALUES (" + itemNum + "," + cart + "," + quantity + ", \"" + size + "\");";
                    else{ //if it is in the table, increase the quantity under the existing entry
                        results.next();
                        int dbQuantity = results.getInt("quantity");
                        quantity += dbQuantity;
                        query = "UPDATE orderItem SET quantity = " + quantity + " WHERE cartID = " + cart + ";";
                    }
                }
                s.update(query);
                popupwindow.hide();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addToOrderBtn.getStyleClass().add("button-AddToCart");

        if(itemNum == 1 || itemNum == 4 || itemNum == 7){
            orderBox.getChildren().addAll(choices, qDisplay, addToOrderBtn);
        }
        else{
            orderBox.getChildren().addAll(choices, radioBtnBox, qDisplay, addToOrderBtn);
        }

        orderBox.setPadding(new Insets(15,15,15,15));
        Scene orderScene = new Scene(orderBox,250,175);
        orderScene.getStylesheets().add("file:///C:/Users/Josh/Desktop/styles.css");
        return orderScene;
    }

    /*
    public static void main(String[] args){
        launch(args);
    }
    */
}
