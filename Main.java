package com.example.ordertime;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LongSummaryStatistics;
import java.util.function.Predicate;

public class Main extends Application {
    //connects to our database or we hope it does
    SQLConnector sqlConn = new SQLConnector("jdbc:mysql://localhost:3306/cs3560f21", "root", "d4rkw01f");
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
    Button updateMenu = new Button("Update Menu");

    Label restaurantName = new Label("Burgerverse");

    Item nuggets = new Item(1);
    Item borg = createItem("borgor.jpg", "Burger");
    Item fry = createItem("fritasYum.jpg", "Fries");
    Item kMeal = createItem("kidsMealOWO.jpeg", "Kids Meal");
    Item milkShake = createItem("milkyshakey.jpg", "Shake");
    Item rbFloat = createItem("rootbeerFloatin.jpg", "Root Beer Float" );

    Stage window;
    Scene updateMenuScene, addItemScene;
//    Connection conn = null;
//    PreparedStatement ps;
    TableView<Item> table;
    Button addItemBtn = new Button();
    Button returnBtn = new Button();
    TextField itemPriceTF;
    TextField itemPicTF;
    TextField itemIdTF;
    TextField itemNameTF;
    TextField isAvailTF;

    public Main() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        //sets up stage to be built!
        window = new Stage();
        window.setTitle("Menu");
        window.setResizable(false);
        window.setScene(getHomePage());
        window.show();
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
        items.add(getMenuItem(nuggets),0,0);
        items.add(getMenuItem(borg),1,0);
        items.add(getMenuItem(fry),2,0);
        items.add(getMenuItem(kMeal),0,1);
        items.add(getMenuItem(milkShake),1,1);
        items.add(getMenuItem(rbFloat),2,1);
        items.setAlignment(Pos.CENTER);
        items.setHgap(5);
        items.setVgap(5);
        VBox centerNavPane = new VBox(title, items);
        centerNavPane.setAlignment(Pos.CENTER);

        Menu menu = new Menu();
        menu.addToMenu(nuggets);
        menu.addToMenu(borg);
        menu.addToMenu(fry);
        menu.addToMenu(kMeal);
        menu.addToMenu(milkShake);
        menu.addToMenu(rbFloat);

        //center with items for food
        comboNav.setFitHeight(50);
        comboNav.setFitWidth(50);
        comboNav.setOnMouseClicked(mouseEvent -> {
            //query for itemID's
            //if they have a "C" in their string, we add item into list, else, we move to next item
            //display combo nav
            System.out.println("Combo Nav clicked");
        });
        drinkNav.setFitWidth(50);
        drinkNav.setFitHeight(50);
        drinkNav.setOnMouseClicked(mouseEvent -> {
            System.out.println("Drink Nav Clicked");
            int row = 0;
            int column = 0;
            centerNavPane.getChildren().removeAll(title,items);
            for(int i =0; i<menu.getMenuSize(); i++){
                if((i+1)%3 == 0){
                    row++;
                    column = 0;
                }
                items.getChildren().remove(column++, row);
            }
            row= 0;
            column = 0;
            for(int i =0 ; i<menu.getMenuSize(); i++){
                if(menu.getItemOnMenu(i).getCategory().equalsIgnoreCase("D")) {
                    if(column%3 ==0){
                        //we start a new row
                        row++;
                        column= 0;
                        items.add(getMenuItem(menu.getItemOnMenu(i)), column, row);
                    }
                    items.add(getMenuItem(menu.getItemOnMenu(i)), column++, row);
                }
            }
            title.setText("Drinks");
            centerNavPane.getChildren().addAll(title, items);
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
        //to be implemented; will go to update menu side
        updateMenu.setOnAction(actionEvent -> {
           window.setTitle("UPDATE MENU");
           window.setScene(updateMenu());
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

        //whole menu setup -- visual of the main menu for customers
        BorderPane menuSetup = new BorderPane();
        menuSetup.setCenter(centerNavPane);
        //need to add switch for when items get clicked
        menuSetup.setTop(topPane);
        menuSetup.setRight(cart);
        menuSetup.setBottom(bottomNavPane);

        Scene scene = new Scene(menuSetup);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        return scene;
    }

    public VBox getMenuItem(Item item){
        Image foodPic = new Image(getClass().getResource(item.getPictureID()).toExternalForm());
        ImageView pic = new ImageView(foodPic);
        pic.setFitWidth(150);
        pic.setFitHeight(150);

        //label for food
        Label itemLabel = new Label(item.getItemName());
        itemLabel.getStyleClass().add("label-Items");
        //label for price
        String price = String.format("$%.2f", item.getPrice());
        Label itemPrice = new Label(price);
        itemPrice.getStyleClass().add("label-Items");
        //labels to be placed under the picture
        HBox labels = new HBox(itemLabel, itemPrice);
        labels.setAlignment(Pos.CENTER);
        labels.setSpacing(10);

        VBox itemOnMenu= new VBox(pic, labels);
        itemOnMenu.setAlignment(Pos.CENTER);
        itemOnMenu.setSpacing(5);

        itemOnMenu.setOnMouseClicked(mouseEvent -> {
             window.setTitle(item.getItemName());
             window.setScene(getItemPage(item));
             window.setWidth(640);
             window.setHeight(640);
        });

        return itemOnMenu;
    }

    public Scene getItemPage(Item item){
        ImageView decor = new ImageView(banner);
        Label restName = new Label("Burgerverse");
        restName.getStyleClass().add("label-Title");

        Image itemImage=  new Image(getClass().getResource(item.getPictureID()).toExternalForm());
        ImageView itemPic = new ImageView(itemImage);
        itemPic.setFitWidth(200);
        itemPic.setFitHeight(200);

        Label iName = new Label(item.getItemName());
        iName.getStyleClass().add("label-Subtitle");

        String pricey = String.format("%.2f", item.getPrice());
        Label iPrice = new Label(pricey);
        iPrice.getStyleClass().add("label-Items");
        Label quantityLabel = new Label("1");
        item.setQuantity(1);

        //allows customer to return to home page
        Button cancel = new Button("cancel");
        cancel.setOnAction(actionEvent -> {
            window.setScene(getHomePage());
            window.setWidth(640);
            window.setHeight(850);
        });
        cancel.getStyleClass().add("button-cancel");

        //allows user to add to the quantity of an item
        Button add = new Button("+");
        add.setOnAction(actionEvent -> {
            item.incrementQuantity();
            quantityLabel.setText(String.valueOf(item.getQuantity()));
        });
        add.getStyleClass().add("button-AddToCart");
        //allows user to subtract the amount of an item they want
        Button subtract = new Button("-");
        subtract.setOnAction(actionEvent ->{
            if(item.getQuantity() == 0)
                quantityLabel.setText(String.valueOf(0));
            else{
                item.decrementQuantity();
                quantityLabel.setText(String.valueOf(item.getQuantity()));
            }
        });
        subtract.getStyleClass().add("button-cancel");
        quantityLabel.getStyleClass().add("label-Items");

        //adds the item with specified quantity into the cart
        Button addItemToCart = new Button("Add to Cart");
        addItemToCart.setOnAction(actionEvent -> {
            if(item.getQuantity() == 0)
                System.out.println("Nothing added to cart; adjust quantity");
            else {
                System.out.println(item.getItemName() + " x" + item.getQuantity() + " has been added to your cart");
                window.setScene(getHomePage());
                window.setWidth(640);
                window.setHeight(850);
            }
        });
        addItemToCart.getStyleClass().add("button-AddToCart");

        //setup for the scene to be returnd eventually
        HBox cancelTop = new HBox(cancel);
        cancelTop.setAlignment(Pos.TOP_LEFT);
        cancelTop.setPadding(new Insets(5,5,5,5));
        HBox itemTitle = new HBox( iName);
        itemTitle.setAlignment(Pos.CENTER);
        VBox topTitle = new VBox(cancelTop, itemTitle);
        topTitle.setAlignment(Pos.CENTER);
        topTitle.setSpacing(5);
        HBox quantityControl = new HBox(subtract, quantityLabel, add);
        quantityControl.setAlignment(Pos.CENTER);
        quantityControl.setSpacing(10);
        HBox addingToCart = new HBox(addItemToCart);
        addingToCart.setAlignment(Pos.BOTTOM_RIGHT);
        addingToCart.setPadding(new Insets(5,5,5,5));
        VBox itemPane = new VBox(restName, decor, cancelTop,itemPic, quantityControl, iPrice, addingToCart);
        itemPane.setAlignment(Pos.CENTER);

        //scene to be  returned
        Scene itemScene = new Scene(itemPane);
        itemScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        return itemScene;
    }

    public Scene updateMenu() {
        Label updateLabel = new Label("Update Menu");

        Label clickUpdateLabel = new Label("Click on an item to update it ");
        Label searchLabel = new Label("Search: ");
        TextField searchField = new TextField();
        searchField.setMaxWidth(300);
        HBox hbox = new HBox(searchLabel, searchField);
        BorderPane clickSearch = new BorderPane();
        clickSearch.setLeft(clickUpdateLabel);
        clickSearch.setRight(hbox);

        addItemBtn = new Button("ADD ITEM");
        addItemBtn.setOnAction(event -> {
            window.setTitle("ADD NEW ITEM");
            window.setScene(addItem());
        });

        TableColumn<Item, String> itemCol = new TableColumn<>("Item");
        itemCol.setMinWidth(400);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(getProduct());
        table.getColumns().addAll(itemCol, priceCol);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(updateLabel, clickSearch, addItemBtn);
        vbox.setAlignment(Pos.CENTER);

        BorderPane updateMenuPane = new BorderPane();
        updateMenuPane.setTop(vbox);
        updateMenuPane.setCenter(table);

        FilteredList<Item> filteredProduct = new FilteredList<>(getProduct(), e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredProduct.setPredicate((Predicate<? super Item>) user -> {
                    if(newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(user.getItemName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Item> sortedProduct = new SortedList<>(filteredProduct);
            sortedProduct.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedProduct);
        });

        updateMenuScene = new Scene(updateMenuPane, 600, 500);
        return updateMenuScene;
    }

    public Scene addItem() {
        Label addItemLabel = new Label("ADD NEW ITEM");

        Label itemIdLabel = new Label("Item ID: ");
        TextField itemIdTF = new TextField();
        HBox itemIdHB = new HBox(itemIdLabel, itemIdTF);

        Label itemName = new Label("Item Name: ");
        TextField itemNameTF = new TextField();
        HBox itemNameHB = new HBox(itemName, itemNameTF);

        Label isAvailLabel = new Label("Item Available: ");
        TextField isAvailTF = new TextField();
        HBox isAvailHB = new HBox(isAvailLabel, isAvailTF);

        Label itemPrice = new Label("Item Price: ");
        TextField itemPriceTF = new TextField();
        HBox itemPriceHB = new HBox(itemPrice, itemPriceTF);

        Label itemPic = new Label("Item Picture: ");
        TextField itemPicTF = new TextField();
        HBox itemPicHB = new HBox(itemPic, itemPicTF);

        Label multSizeLabel = new Label("Multiple Sizes? ");
        ToggleGroup yesNoGroup = new ToggleGroup();

        RadioButton yesRB = new RadioButton("Yes");
        yesRB.setToggleGroup(yesNoGroup);
        yesRB.setSelected(true);

        RadioButton noRB = new RadioButton("No");
        noRB.setToggleGroup(yesNoGroup);
        noRB.setSelected(true);

        CheckBox smallCB = new CheckBox("Small");
        CheckBox mediumCB = new CheckBox("Medium");
        CheckBox largeCB = new CheckBox("Large");

        HBox sizesHB = new HBox(multSizeLabel, yesRB, noRB);
        yesNoGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                if (noRB.isSelected()) {
                    sizesHB.getChildren().removeAll(smallCB, mediumCB, largeCB);
                }
                if (yesRB.isSelected()) {
                    sizesHB.getChildren().addAll(smallCB, mediumCB, largeCB);
                }
            }
        });

        Button saveItem = new Button("Save item");
//        saveItem.setOnMouseClicked(event -> {
//            if(itemIdTF.getText() != null || itemNameTF.getText() != null
//                    || isAvailTF.getText() != null || itemPriceTF.getText() != null) {
//                try {
//                    conn = MySqlConnection();
//                    ps = conn.prepareStatement("insert into item"
//                            + "(itemID, itemName, isAvailable, price) values (?, ?, ?, ?)");
//                    ps.setInt(1, Integer.parseInt(itemIdTF.getText()));
//                    ps.setString(2, itemNameTF.getText());
//                    ps.setInt(3, Integer.parseInt(isAvailTF.getText()));
//                    ps.setDouble(4, Double.parseDouble(itemPriceTF.getText()));
//                    int res = ps.executeUpdate();
//                    if (res >= 1) {
//                        System.out.println("Add Item Successful");
//                    } else {
//                        System.out.println("Add Item Failed");
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(CS3560KioskProject.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                itemIdTF.setText("");
//                isAvailTF.setText("");
//                isAvailTF.setText("");
//                itemPriceTF.setText("");
//            }
//        });



        returnBtn = new Button("Cancel");
        returnBtn.setOnAction(e -> {
            window.setScene(updateMenu());
        });

        HBox saveReturnVB = new HBox(saveItem, returnBtn);
        saveReturnVB.setAlignment(Pos.CENTER);

        VBox addItemVB = new VBox();
        addItemVB.setSpacing(40);
        addItemVB.setPadding(new Insets(10, 10, 10, 10));
        addItemVB.getChildren().addAll(addItemLabel, itemIdHB, itemNameHB,
                isAvailHB, itemPriceHB, itemPicHB,
                sizesHB, saveReturnVB);
        addItemVB.setAlignment(Pos.TOP_CENTER);

        BorderPane addItemPane = new BorderPane(addItemVB);
        addItemScene = new Scene(addItemPane, 600, 500);
        return addItemScene;
    }

    //maybe add the different scenes here as methods
    // then have main be a sorter of what happens

    public Scene editItem() {
        Label addItemLabel = new Label("EDIT ITEM");

        Label itemIdLabel = new Label("Item ID: ");
        itemIdTF = new TextField();
        HBox itemIdHB = new HBox(itemIdLabel, itemIdTF);

        Label itemName = new Label("Item Name: ");
        itemNameTF = new TextField();
        HBox itemNameHB = new HBox(itemName, itemNameTF);

        Label isAvailLabel = new Label("Item Available: ");
        isAvailTF = new TextField();
        HBox isAvailHB = new HBox(isAvailLabel, isAvailTF);

        Label itemPrice = new Label("Item Price: ");
        itemPriceTF = new TextField();
        HBox itemPriceHB = new HBox(itemPrice, itemPriceTF);

        Label itemPic = new Label("Item Picture: ");
        itemPicTF = new TextField();
        HBox itemPicHB = new HBox(itemPic, itemPicTF);

        Label multSizeLabel = new Label("Multiple Sizes? ");
        ToggleGroup yesNoGroup = new ToggleGroup();

        RadioButton yesRB = new RadioButton("Yes");
        yesRB.setToggleGroup(yesNoGroup);
        yesRB.setSelected(true);

        RadioButton noRB = new RadioButton("No");
        noRB.setToggleGroup(yesNoGroup);
        noRB.setSelected(true);

        CheckBox smallCB = new CheckBox("Small");
        CheckBox mediumCB = new CheckBox("Medium");
        CheckBox largeCB = new CheckBox("Large");

        HBox sizesHB = new HBox(multSizeLabel, yesRB, noRB);
        yesNoGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                if (noRB.isSelected()) {
                    sizesHB.getChildren().removeAll(smallCB, mediumCB, largeCB);
                }
                if (yesRB.isSelected()) {
                    sizesHB.getChildren().addAll(smallCB, mediumCB, largeCB);
                }
            }
        });

        Button updateItem = new Button("Update item");
//        updateItem.setOnMouseClicked(event -> {
//            if (itemIdTF != null || itemNameTF != null
//                    || isAvailTF != null || itemPriceTF != null) {
//                try {
//                    conn = MySqlConnection();
//
//
//                    ps = conn.prepareStatement("update item set "
//                            + "itemID = ?, itemName = ?, isAvailable = ?, price = ? where itemID = " + oldItemID);
//                    ps.setInt(1, Integer.parseInt(itemIdTF.getText()));
//                    ps.setString(2, itemNameTF.getText());
//                    ps.setInt(3, Integer.parseInt(isAvailTF.getText()));
//                    ps.setDouble(4, Double.parseDouble(itemPriceTF.getText()));
//
//                    int res = ps.executeUpdate();
//                    if (res >= 1) {
//                        System.out.println("Item Successfully Updated");
//                    } else {
//                        System.out.println("Item Failed to Update");
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(CS3560KioskProject.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        });
//
//        showItemToFields(ind);

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            window.setTitle("UPDATE MENU");
            window.setScene(updateMenu());
        });

        HBox saveReturnVB = new HBox(updateItem, returnBtn);
        saveReturnVB.setAlignment(Pos.CENTER);

        VBox addItemVB = new VBox();
        addItemVB.setSpacing(40);
        addItemVB.setPadding(new Insets(10, 10, 10, 10));
        addItemVB.getChildren().addAll(addItemLabel, itemIdHB, itemNameHB,
                isAvailHB, itemPriceHB, itemPicHB,
                sizesHB, saveReturnVB);
        addItemVB.setAlignment(Pos.TOP_CENTER);

        BorderPane addItemPane = new BorderPane(addItemVB);
        addItemScene = new Scene(addItemPane, 600, 500);
        return addItemScene;
    }

    //doesn't work yet
    public ObservableList<Item> getProduct() {
        ObservableList<Item> products = FXCollections.observableArrayList();
//        products.add(nuggets);
//        products.add(borg);
//        products.add(fry);
//        products.add(kMeal);
//        products.add(rbFloat);
//        products.add(milkShake);
        return products;
    }

    private Item createItem(String picture, String itemNameInDB) throws SQLException {
        CachedRowSet r = sqlConn.query("SELECT * FROM item WHERE itemName = \"" + itemNameInDB +"\"");
        r.next();
        Item i = new Item(1);
        return i;
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }

}
