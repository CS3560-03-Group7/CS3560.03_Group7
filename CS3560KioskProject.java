/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3560.kiosk.project;


import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.table.DefaultTableModel;




public class CS3560KioskProject extends Application {
    Stage window;
    Scene updateMenuScene, addItemScene;
    Connection conn = null;
    PreparedStatement ps;
    TableView<Product> table;
    Button addItemBtn = new Button();
    Button returnBtn = new Button();
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        window = primaryStage;
        MySqlConnection();
        window.setTitle("UPDATE MENU");
        window.setScene(updateMenu());
        window.show();
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
               
        TableColumn<Product, String> itemCol = new TableColumn<>("Item");
        itemCol.setMinWidth(400);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
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
        
        FilteredList<Product> filteredProduct = new FilteredList<>(getProduct(), e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredProduct.setPredicate((Predicate<? super Product>) user -> {
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
            SortedList<Product> sortedProduct = new SortedList<>(filteredProduct);
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
            saveItem.setOnMouseClicked(event -> {
                if(itemIdTF.getText() != null || itemNameTF.getText() != null
                            || isAvailTF.getText() != null || itemPriceTF.getText() != null) {
                        try { 
                            conn = MySqlConnection();
                            ps = conn.prepareStatement("insert into item"
                                    + "(itemID, itemName, isAvailable, price) values (?, ?, ?, ?)");
                            ps.setInt(1, Integer.parseInt(itemIdTF.getText()));
                            ps.setString(2, itemNameTF.getText());
                            ps.setInt(3, Integer.parseInt(isAvailTF.getText()));
                            ps.setDouble(4, Double.parseDouble(itemPriceTF.getText()));
                            int res = ps.executeUpdate();
                            if (res >= 1) {
                                System.out.println("Add Item Successful");
                            } else {
                                System.out.println("Add Item Failed");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(CS3560KioskProject.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        itemIdTF.setText("");
                        isAvailTF.setText("");
                        isAvailTF.setText("");
                        itemPriceTF.setText("");
                }
            });
            

            
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
    
    public Connection MySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localHost:3306/cs3560f21", "root", "root");
            System.out.println("MySQL connection Successful");
            return conn;
        } catch (Exception e) {
            System.out.println("MySQL connection Failed");
            return null;
        }
    }
    
//    public ArrayList<Product> retrieveData() {
//        ArrayList<Product> al = null;
//        al = new ArrayList<Product>();
//        try {
//            conn = MySqlConnection();
//            String qry = "select * from item";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(qry);
//            Product item;
//            while (rs.next()) {
//                item = new Product(rs.getInt(1), rs.getString("itemName"),
//                        rs.getInt(3), Double.parseDouble(rs.getString(4)));
//                al.add(item);
//            }
//        } catch (Exception e){
//            System.out.println("Error in retrieveData method: " + e);
//        }
//        return al;
//    }
//    
//    public void fillTable() {
//        ArrayList<Product> al = retrieveData();
//        DefaultTableModel model =(DefaultTableModel)table.getModel();
//        model.setRowCount(0);
//        Object[] row = new Object[4];
//        for (int i = 0; i < al.size(); i++) {
//            row[0] = al.get(i).getItemID();
//            row[1] = al.get(i).getItemName();
//            row[2] = al.get(i).getIsAvailable();
//            row[3] = al.get(i).getPrice();
//            model.addRow(row);
//        }
//    }
    
    public ObservableList<Product> getProduct() {
        ObservableList<Product> products = FXCollections.observableArrayList();
//        products.add(new Product(1, "Burger", 0, 5.99));
//        products.add(new Product(2, "Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
//        
//        products.add(new Product("Burger", 5.99));
//        products.add(new Product("Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
//        products.add(new Product("Burger", 5.99));
//        products.add(new Product("Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
//        products.add(new Product("Burger", 5.99));
//        products.add(new Product("Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
//        products.add(new Product("Burger", 5.99));
//        products.add(new Product("Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
//        products.add(new Product("Burger", 5.99));
//        products.add(new Product("Fries", 2.99));
//        products.add(new Product("Soda", 1.99));
//        products.add(new Product("Shake", 4.99));
        
        return products;
    }

    public static void main(String[] args) {
        launch(args);
    }
    

    
}
