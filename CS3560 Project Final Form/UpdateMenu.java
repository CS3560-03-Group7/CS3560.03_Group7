/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class UpdateMenu extends Application {
    //Stage window;
    //Stage stage;
    Scene updateMenuScene, addItemScene;
    Connection conn = null;
    PreparedStatement ps;
    TableView<Item> table;
    TextField itemIdTF;
    TextField itemNameTF;
    ComboBox comboBox;
    TextField isAvailTF;
    TextField itemPriceTF;
    TextField itemPicTF;
    Button addItemBtn = new Button();
    Button returnBtn = new Button();
    int ind;
    int oldItemID;
    String photopath = "";
    SQLConnector s;
    
    public UpdateMenu(SQLConnector s){
        this.s = s;
    }
    
    @Override
    public void start(Stage stage) throws FileNotFoundException {        
        stage.setTitle("Update Menu");
        stage.setScene(updateMenu(stage));
        stage.show();
    }
    
    public Scene updateMenu(Stage stage) {
        Label updateLabel = new Label("Update Menu");
        
        Label clickUpdateLabel = new Label("Click on an item to update it ");
        Label searchLabel = new Label("Search: ");
        TextField searchField = new TextField();
        searchField.setMaxWidth(300);
        HBox hbox = new HBox(searchLabel, searchField);
        BorderPane clickSearch = new BorderPane();
        clickSearch.setLeft(clickUpdateLabel);
        clickSearch.setRight(hbox);
        
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            stage.setTitle("Main Menu");
            MainMenu main = new MainMenu(s);
            try {
                stage.setScene(main.getHomePage(stage));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UpdateMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        addItemBtn = new Button("Add Item");
        addItemBtn.setOnAction(event -> {
            stage.setTitle("Add New Item");
            stage.setScene(addItem(stage));
            });
        
        BorderPane buttonContainer = new BorderPane();
        buttonContainer.setLeft(backBtn);
        buttonContainer.setRight(addItemBtn);
        buttonContainer.setPadding(new Insets(10,0,10,0));
        
        clickSearch.setBottom(buttonContainer);
        clickSearch.setPadding(new Insets(0,10,0,10));
        
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
        table.setItems(getProduct());
        table.getColumns().addAll(itemIdCol, itemCol, isAvailCol, priceCol);
        
        table.setOnMouseClicked(event -> {
            ind = table.getSelectionModel().getSelectedIndex();
            oldItemID = table.getSelectionModel().getSelectedItem().getItemID();
            stage.setTitle("Edit Item");            
            stage.setScene(editItem(stage));
        });
        
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(updateLabel, clickSearch);
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
    
    public Scene addItem(Stage stage) {
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
            if (itemNameTF.getText() != null
                    || isAvailTF.getText() != null || itemPriceTF.getText() != null) {
                try {
                    conn = MySqlConnection();
                    
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
                    Logger.getLogger(UpdateMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                //itemIdTF.setText("");
                itemNameTF.setText("");
                isAvailTF.setText("");
                itemPriceTF.setText("");
                itemPicTF.setText("");
            }
        });

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            stage.setTitle("Update Menu");
            stage.setScene(updateMenu(stage));
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
    
    public Scene editItem(Stage stage) {
        Label addItemLabel = new Label("EDIT ITEM");

        /*Label itemIdLabel = new Label("Item ID: ");
        itemIdTF = new TextField();
        itemIdTF.setEditable(false);
        HBox itemIdHB = new HBox(itemIdLabel, itemIdTF);*/

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
                    conn = MySqlConnection();
                    
                    
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
                    Logger.getLogger(UpdateMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        showItemToFields(ind);
        
        Button deleteItem = new Button("Delete");
        deleteItem.setOnMouseClicked(deleteEvent -> {
            if (itemIdTF != null || itemNameTF != null
                    || isAvailTF != null || itemPriceTF != null) {
                try {
                    conn = MySqlConnection();
                    
                    
                    ps = conn.prepareStatement("delete from item where itemID = ?");
                    ps.setInt(1, Integer.parseInt(itemIdTF.getText()));
                    
                    int res = ps.executeUpdate();
                    if (res >= 1) {
                        System.out.println("Item Successfully Deleted");
                    } else {
                        System.out.println("Item Failed to Delete");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UpdateMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            stage.setTitle("Update Menu");
            stage.setScene(updateMenu(stage));
        });

        HBox saveReturnVB = new HBox(updateItem, deleteItem, returnBtn);
        saveReturnVB.setSpacing(30);
        saveReturnVB.setAlignment(Pos.CENTER);

        VBox addItemVB = new VBox();
        addItemVB.setSpacing(40);
        addItemVB.setPadding(new Insets(10, 10, 10, 10));
        //addItemVB.getChildren().addAll(addItemLabel, itemIdHB, itemNameHB, categoryHB, isAvailHB, itemPriceHB, itemPicHB, saveReturnVB);
        addItemVB.getChildren().addAll(addItemLabel, itemNameHB, categoryHB, isAvailHB, itemPriceHB, itemPicHB, saveReturnVB);
        addItemVB.setAlignment(Pos.TOP_CENTER);

        BorderPane addItemPane = new BorderPane(addItemVB);
        addItemScene = new Scene(addItemPane, 600, 500);
        return addItemScene;
    }
    
    public void showItemToFields(int index) {
        //itemIdTF.setText(Integer.toString(getProduct().get(index).getItemID()));
        itemNameTF.setText(getProduct().get(index).getItemName());
        comboBox.setValue(getProduct().get(index).getCategory());
        isAvailTF.setText(Integer.toString(getProduct().get(index).getIsAvailable()));
        itemPriceTF.setText(Double.toString(getProduct().get(index).getPrice()));
        //itemPicTF.setText(getProduct().get(index).getPicture());    
        
    }
    
    public Connection MySqlConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localHost:3306/cs3560f21", "root", "CS3560@");
            System.out.println("MySQL connection Successful");
            return conn;
        } catch (Exception e) {
            System.out.println("MySQL connection Failed");
            return null;
        }
    }
 
    public ObservableList<Item> getProduct() {
        ObservableList<Item> products = FXCollections.observableArrayList();
        try {
            conn = MySqlConnection();
            ResultSet rs = conn.createStatement().executeQuery("select * from item");
            while (rs.next()) {
                products.add(new Item(rs.getInt(1), rs.getString("itemName"), 
                        rs.getString("category"), rs.getInt("isAvailable"), 
                        rs.getDouble("price"), rs.getString("picture")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return products;
    }
    /*
    public static void main(String[] args) {
        launch(args);
    }*/
     
}
