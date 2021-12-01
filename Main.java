package com.example.ordertime;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.function.Predicate;

public class Main extends Application {
    //connects to our database or we hope it does
    SQLConnector sqlConn = new SQLConnector("", "", "");
    //side navigation images
    Image banner = new Image(getClass().getResource("banner1.png").toExternalForm());
    ImageView topBanner = new ImageView(banner);

    //buttons at bottom
    Button cancel = new Button("Cancel Order");
    Button completeOrder = new Button("Complete Order");
    Button updateMenu = new Button("Update Menu");

    Label restaurantName = new Label("Burgerverse");

    Item nuggets = new Item(4,"Dino Nuggies", "E", 0, 7.0, "dinoNuggiesAreCool.jpg" );
    Item borg = new Item(1, "Burger", "E", 0, 9.0, "borgor.jpg" );
    Item fry = new Item(2,"Fries", "S", 0, 2.5, "fritasYum.jpg" );
    Item kMeal = new Item(6,"Kids Meal", "C", 0, 6.0, "kidsMealOWO.jpeg" );
    Item milkShake= new Item(3,"Shake", "D", 0, 4.0, "milkyshakey.jpg");
    Item rbFloat = new Item(8,"Root Beer Float", "D", 0, 6.5, "rootbeerFloatin.jpg" );

    Stage window;
    Scene updateMenuScene, addItemScene;
    TableView<Item> table;
    Button addItemBtn = new Button();
    Button returnBtn = new Button();
    TextField itemPriceTF;
    TextField itemPicTF;
    TextField itemIdTF;
    TextField itemNameTF;
    TextField isAvailTF;
    int ind;
    int oldItemID;
    ComboBox comboBox;
    Connection conn = null;
    PreparedStatement ps;

    public Main() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        //sets up stage to be built!
        window = new Stage();
        window.setTitle("Menu");
        window.setResizable(false);
        window.setWidth(640);
        window.setHeight(800);
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
//            FilteredList<Item> filteredProduct = new FilteredList<>(getProduct(), e -> true);
//            searchText.setOnKeyReleased(e -> {
//                searchText.textProperty().addListener((observableValue, oldValue, newValue) -> {
//                    filteredProduct.setPredicate((Predicate<? super Item>) user -> {
//                        if(newValue == null || newValue.isEmpty()) {
//                            return true;
//                        }
//                        String lowerCaseFilter = newValue.toLowerCase();
//                        if(user.getItemName().toLowerCase().contains(lowerCaseFilter)) {
//                            return true;
//                        }
//                        return false;
//                    });
//                });
//                SortedList<Item> sortedProduct = new SortedList<>(filteredProduct);
//                sortedProduct.comparatorProperty().bind(table.comparatorProperty());
//                table.setItems(sortedProduct);
//            });
        //side navigation bar that will filter foods

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
        bottomNavPane.setPadding(new Insets(10,10,10,10));

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
        VBox topPane = new VBox(updates, restaurantName, topBanner);
        topPane.setAlignment(Pos.CENTER);

        //side nav for cart

        //whole menu setup -- visual of the main menu for customers
        BorderPane menuSetup = new BorderPane();
        menuSetup.setCenter(centerNavPane);
        //need to add switch for when items get clicked
        menuSetup.setTop(topPane);
        menuSetup.setBottom(bottomNavPane);

        Scene scene = new Scene(menuSetup);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        return scene;
    }

    public VBox getMenuItem(Item item){

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

        //fix the picture bad bois cause theyre being pieces of shit
        Image foodPic = new Image(getClass().getResource(item.getPictureID()).toExternalForm());
        ImageView pic = new ImageView(foodPic);
        pic.setFitWidth(150);
        pic.setFitHeight(150);
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

        TableColumn<Item, Integer> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setMinWidth(50);
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));

        TableColumn<Item, String> itemCol = new TableColumn<>("Item");
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Item, Integer> isAvailCol = new TableColumn<>("Available");
        isAvailCol.setMinWidth(50);
        isAvailCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(50);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        table.setItems(getProduct());
        table.getColumns().addAll(itemIdCol, itemCol, isAvailCol, priceCol);

        table.setOnMouseClicked(event -> {
            ind = table.getSelectionModel().getSelectedIndex();
            oldItemID = table.getSelectionModel().getSelectedItem().getItemID();
            window.setTitle("Edit ITEM");
            window.setScene(editItem());
        });

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(updateLabel, clickSearch, addItemBtn);
        vbox.setAlignment(Pos.CENTER);

        BorderPane updateMenuPane = new BorderPane();
        updateMenuPane.setTop(vbox);
        updateMenuPane.setCenter(table);

//        FilteredList<Item> filteredProduct = new FilteredList<>(getProduct(), e -> true);
//        searchField.setOnKeyReleased(e -> {
//            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
//                filteredProduct.setPredicate((Predicate<? super Item>) user -> {
//                    if(newValue == null || newValue.isEmpty()) {
//                        return true;
//                    }
//                    String lowerCaseFilter = newValue.toLowerCase();
//                    if(user.getItemName().toLowerCase().contains(lowerCaseFilter)) {
//                        return true;
//                    }
//                    return false;
//                });
//            });
//            SortedList<Item> sortedProduct = new SortedList<>(filteredProduct);
//            sortedProduct.comparatorProperty().bind(table.comparatorProperty());
//            table.setItems(sortedProduct);
//        });

        updateMenuScene = new Scene(updateMenuPane, 600, 500);
        return updateMenuScene;
    }

    public Scene addItem() {
        Label addItemLabel = new Label("ADD NEW ITEM");

        Label itemName = new Label("Item Name: ");
        itemNameTF = new TextField();
        HBox itemNameHB = new HBox(itemName, itemNameTF);

        Label category = new Label("Category: ");
        comboBox = new ComboBox();
        comboBox.getItems().addAll("E", "S", "D", "C");
        HBox categoryHB = new HBox(category, comboBox);

        Label isAvailLabel = new Label("Item Available: ");
        isAvailTF = new TextField();
        HBox isAvailHB = new HBox(isAvailLabel, isAvailTF);

        Label itemPrice = new Label("Item Price: ");
        itemPriceTF = new TextField();
        HBox itemPriceHB = new HBox(itemPrice, itemPriceTF);

        Label itemPic = new Label("Item Picture: ");
        itemPicTF = new TextField();
        HBox itemPicHB = new HBox(itemPic, itemPicTF);

        Button saveItem = new Button("Save item");
        saveItem.setOnMouseClicked(event -> {
            if (itemIdTF.getText() != null || itemNameTF.getText() != null
                    || isAvailTF.getText() != null || itemPriceTF.getText() != null) {
                try {
                    conn = (Connection) new SQLConnector("",
                            "", "");

                    ps = conn.prepareStatement("insert into item"
                            + "(itemName, category, isAvailable, price, picture) values (?, ?, ?, ?, ?)");
                    ps.setString(1, itemNameTF.getText());
                    ps.setString(2, comboBox.getValue().toString());
                    ps.setInt(3, Integer.parseInt(isAvailTF.getText()));
                    ps.setDouble(4, Double.parseDouble(itemPriceTF.getText()));
                    ps.setString(5, itemPicTF.getText());
                    int res = ps.executeUpdate();
                    if (res >= 1) {
                        System.out.println("Add Item Successful");
                    } else {
                        System.out.println("Add Item Failed");
                    }
                } catch (SQLException ex) {
                    //idk where this comes from is what we say
                    System.out.println("Bitch don't work");
                }
                itemIdTF.setText("");
                itemNameTF.setText("");
                isAvailTF.setText("");
                itemPriceTF.setText("");
                itemPicTF.setText("");
            }
        });

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            window.setTitle("UPDATE MENU");
            window.setScene(updateMenu());
        });

        HBox saveReturnVB = new HBox(saveItem, returnBtn);
        saveReturnVB.setSpacing(30);
        saveReturnVB.setAlignment(Pos.CENTER);

        VBox addItemVB = new VBox();
        addItemVB.setSpacing(40);
        addItemVB.setPadding(new Insets(10, 10, 10, 10));
        addItemVB.getChildren().addAll(addItemLabel, itemNameHB,
                categoryHB, isAvailHB, itemPriceHB, itemPicHB, saveReturnVB);
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
        itemIdTF.setEditable(false);
        HBox itemIdHB = new HBox(itemIdLabel, itemIdTF);

        Label itemName = new Label("Item Name: ");
        itemNameTF = new TextField();
        HBox itemNameHB = new HBox(itemName, itemNameTF);

        Label category = new Label("Category: ");
        comboBox = new ComboBox();
        comboBox.getItems().addAll("E", "S", "D", "C");
        HBox categoryHB = new HBox(category, comboBox);

        Label isAvailLabel = new Label("Item Available: ");
        isAvailTF = new TextField();
        HBox isAvailHB = new HBox(isAvailLabel, isAvailTF);

        Label itemPrice = new Label("Item Price: ");
        itemPriceTF = new TextField();
        HBox itemPriceHB = new HBox(itemPrice, itemPriceTF);

        Label itemPic = new Label("Item Picture: ");
        itemPicTF = new TextField();
        HBox itemPicHB = new HBox(itemPic, itemPicTF);

        Button updateItem = new Button("Update item");
        updateItem.setOnMouseClicked(event -> {
            if (itemIdTF != null || itemNameTF != null
                    || isAvailTF != null || itemPriceTF != null) {
                try {
                    conn =(Connection) new SQLConnector("jdbc:mysql://localhost:3306/cs3560f21",
                            "root", "d4rkw01f");


                    ps = conn.prepareStatement("update item set "
                            + "itemName = ?, category = ?, isAvailable = ?, price = ? where itemID = " + oldItemID);
                    ps.setString(1, itemNameTF.getText());
                    ps.setString(2,comboBox.getValue().toString());
                    ps.setInt(3,Integer.parseInt(isAvailTF.getText()));
                    ps.setDouble(4,Double.parseDouble(itemPriceTF.getText()));

                    int res = ps.executeUpdate();
                    if (res >= 1) {
                        System.out.println("Item Successfully Updated");
                    } else {
                        System.out.println("Item Failed to Update");
                    }
                } catch (SQLException ex) {
                    System.out.println("owo why you no work");
                }

            }
        });

//        showItemToFields(ind);

        Button deleteItem = new Button("Delete");
        deleteItem.setOnMouseClicked(deleteEvent -> {
            if (itemIdTF != null || itemNameTF != null
                    || isAvailTF != null || itemPriceTF != null) {
                try {
                    conn = (Connection) new SQLConnector("",
                            "", "");


                    ps = conn.prepareStatement("delete from item where itemID = ?");
                    ps.setInt(1, Integer.parseInt(itemIdTF.getText()));

                    int res = ps.executeUpdate();
                    if (res >= 1) {
                        System.out.println("Item Successfully Deleted");
                    } else {
                        System.out.println("Item Failed to Delete");
                    }
                } catch (SQLException ex) {
                    System.out.println("Other shit don't work");
                }

            }
        });

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            window.setTitle("UPDATE MENU");
            window.setScene(updateMenu());
        });

        HBox saveReturnVB = new HBox(updateItem, deleteItem, returnBtn);
        saveReturnVB.setSpacing(30);
        saveReturnVB.setAlignment(Pos.CENTER);

        VBox addItemVB = new VBox();
        addItemVB.setSpacing(40);
        addItemVB.setPadding(new Insets(10, 10, 10, 10));
        addItemVB.getChildren().addAll(addItemLabel, itemIdHB, itemNameHB,
                categoryHB, isAvailHB, itemPriceHB, itemPicHB, saveReturnVB);
        addItemVB.setAlignment(Pos.TOP_CENTER);

        BorderPane addItemPane = new BorderPane(addItemVB);
        addItemScene = new Scene(addItemPane, 600, 500);
        return addItemScene;
    }
//
//    public void showItemToFields(int index) {
//        itemIdTF.setText(Integer.toString(getProduct().get(index).getItemID()));
//        itemNameTF.setText(getProduct().get(index).getItemName());
//        comboBox.setValue(getProduct().get(index).getCategory());
//        isAvailTF.setText(Integer.toString(getProduct().get(index).getIsAvailable()));
//        itemPriceTF.setText(Double.toString(getProduct().get(index).getPrice()));
//        itemPicTF.setText(getProduct().get(index).getPictureID());
//    }

    //doesn't work yet
//    public ObservableList<Item> getProduct() {
//        ObservableList<Item> products = FXCollections.observableArrayList();
//        try {
//            conn = (Connection) new SQLConnector("",
//                                                "", "");
//            ResultSet rs = conn.createStatement().executeQuery("select * from item");
//            while (rs.next()) {
//                products.add(new Item(rs.getInt(1), rs.getString("itemName"),
//                        rs.getString("category"), rs.getInt(4),
//                        Double.parseDouble(rs.getString(5)), rs.getString("picture")));
//            }
//        } catch (SQLException ex) {
//            System.out.println("No clue what is going on but here we are");
//        }
//
//        return products;
//    }

}
