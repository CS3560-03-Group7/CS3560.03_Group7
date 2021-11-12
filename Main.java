package com.example.ordertime;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Main extends Application {
    //side navigation images
    Image banner = new Image(getClass().getResource("banner1.png").toExternalForm());
    ImageView topBanner = new ImageView(banner);

    Image combo = new Image(getClass().getResource("comboNav.png").toExternalForm());
    ImageView comboNav = new ImageView(combo);

    Image drink = new Image(getClass().getResource("drinkNav.png").toExternalForm());
    ImageView drinkNav = new ImageView(drink);

    Image kidsMeal = new Image(getClass().getResource("kmNav.png").toExternalForm());
    ImageView kmNav = new ImageView(kidsMeal);

    Image meal = new Image(getClass().getResource("mealNav.png").toExternalForm());
    ImageView mealNav = new ImageView(meal);

    Image sides = new Image(getClass().getResource("sidesNav.png").toExternalForm());
    ImageView sideNav = new ImageView(sides);

    //buttons at bottom
    Button cancel = new Button("Cancel Order");
    Button completeOrder = new Button("Complete Order");

    Label restaurantName = new Label("Burgerverse");

    Item nuggets = new Item("F-000001", "Dino Nuggies", 7.99, "dinoNuggiesAreCool.jpg", 10 );
    Item borg = new Item("F-00002", "Burgers", 6.95, "borgor.jpg", 0);
    Item fry = new Item("F-00003", "French Fries", 4.50, "fritasYum.jpg", 0);
    Item kMeal = new Item("C-00004", "Kids Meal", 8.50, "kidsMealOWO.jpeg", 0);
    Item milkShake = new Item("D-00005", "Milkshake", 4.0, "milkyshakey.jpg", 0);
    Item rbFloat = new Item("D-00006", "Rootbeer Float", 4.0, "rootbeerFloatin.jpg",0 );

    @Override
    public void start(Stage primaryStage) {
        Menu menu = new Menu();
        menu.addToMenu(nuggets);
        menu.addToMenu(borg);
        menu.addToMenu(fry);
        menu.addToMenu(kMeal);
        menu.addToMenu(milkShake);
        menu.addToMenu(rbFloat);
        
        //sets up scene and adds stylessheets**
        Scene scene = getHomePage();
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        //sets up stage to be built!
        primaryStage.setTitle("Menu");
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(950);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Scene getHomePage(){
        //Now we looking at the GUI
        //search bar at top of menu
        TextField searchText = new TextField();
        Label search = new Label("Search ");
        HBox searchBar = new HBox(search, searchText);
        searchBar.setAlignment(Pos.TOP_RIGHT);
        searchBar.setSpacing(5);

        //Title for top of menu
        Label title = new Label("Popular Items!");
        title.getStyleClass().add("label-Subtitle");

        //for all your menu needs
        //gridpane with vboxes -- possibly edit so that it can be
        GridPane items = new GridPane();
        items.add(nuggets.getMenuItem(),0,0);
        items.add(borg.getMenuItem(),1,0);
        items.add(fry.getMenuItem(),2,0);
        items.add(kMeal.getMenuItem(),0,1);
        items.add(milkShake.getMenuItem(),1,1);
        items.add(rbFloat.getMenuItem(),2,1);
        items.setAlignment(Pos.CENTER);
        items.setHgap(5);
        items.setVgap(5);
        VBox centerNavPane = new VBox(title, items);
        centerNavPane.setAlignment(Pos.CENTER);

        //center with items for food
        comboNav.setFitHeight(50);
        comboNav.setFitWidth(50);
        comboNav.setOnMouseClicked(mouseEvent -> {
            //need database for this bad boy so we can get the items with their itemID
            System.out.println("Combo Nav Clicked");
        });
        drinkNav.setFitWidth(50);
        drinkNav.setFitHeight(50);
        drinkNav.setOnMouseClicked(mouseEvent -> {
            System.out.println("Drink Nav Clicked");
        });
        kmNav.setFitHeight(50);
        kmNav.setFitWidth(50);
        kmNav.setOnMouseClicked(mouseEvent -> {
            System.out.println("Kids Meal Nav clicked");
        });
        mealNav.setFitWidth(50);
        mealNav.setFitHeight(50);
        mealNav.setOnMouseClicked(mouseEvent -> {
            System.out.println("Meal Nav clicked");
        });
        sideNav.setFitHeight(50);
        sideNav.setFitWidth(50);
        sideNav.setOnMouseClicked(mouseEvent -> {
            System.out.println("Sides nav clicked");
        });
        //side navigation bar that will filter foods
        HBox filterPane = new HBox(comboNav, mealNav, kmNav, drinkNav, sideNav);
        filterPane.setSpacing(5);
        filterPane.setAlignment(Pos.CENTER);
        filterPane.setPadding(new Insets(10,10,10,10));

        //buttons to cancel or complete the order
        completeOrder.setOnAction(actionEvent ->{
            System.out.println("Order completed!");
        });
        completeOrder.getStyleClass().add("button-completeOrder");

        //currently tracks if the cancel button was clicked or not; nothing special for now
        cancel.setOnAction(actionEvent -> {
            System.out.println("Order Cancelled");
        });
        cancel.getStyleClass().add("button-cancel");

        //bottom navigation pane
        HBox bottomNavPane = new HBox(completeOrder, cancel);
        bottomNavPane.setSpacing(10);
        bottomNavPane.setAlignment(Pos.CENTER);

        //top navigation pane
        Button updateMenu = new Button("Update Menu");
        //to be implemented; will go to update menu side
        updateMenu.setOnAction(actionEvent -> {
            System.out.println("Updating Menu");
        });
        HBox updates = new HBox(updateMenu);
        updates.setAlignment(Pos.TOP_RIGHT);
        restaurantName.getStyleClass().add("label-Title");
        restaurantName.getFont().getClass().getResource("future.ttf");
        VBox topPane = new VBox(updates, restaurantName, topBanner, filterPane);
        topPane.setAlignment(Pos.CENTER);

        //side nav for cart
        Label cart = new Label("Cart");
        cart.setAlignment(Pos.CENTER);
        cart.setPadding(new Insets(20,20,20,20));
        cart.getStyleClass().add("label-Subtitle");

        //whole menu setup
        BorderPane menuSetup = new BorderPane();
        menuSetup.setCenter(centerNavPane);
        //need to add switch for when items get clicked
        menuSetup.setTop(topPane);
        menuSetup.setRight(cart);
        menuSetup.setBottom(bottomNavPane);
        Scene scene = new Scene(menuSetup);

        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
