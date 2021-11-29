/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.order.main;

/**
 *
 * @author Josh
 */

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sql.rowset.CachedRowSet;

public class Cart extends Application{
    
    class QuantityColumn{
        private Button removeBtn = new Button("-");
        private Button addBtn = new Button("+");
        private int quantity;        
        private HBox colDisplay = new HBox();
        Label quantityLbl;
        
        public QuantityColumn(int quantity){
            this.quantity = quantity;
            quantityLbl = new Label(Integer.toString(quantity));
            
            colDisplay.setAlignment(Pos.BASELINE_CENTER);
            colDisplay.setSpacing(10);
            colDisplay.getChildren().addAll(removeBtn,quantityLbl,addBtn);
            
            addBtn.setOnAction(e-> {
                add();
            });
            
            removeBtn.setOnAction(e -> {
                remove();
            });
        }
        
        private void add(){
            this.quantity += 1;
            this.quantityLbl.setText(Integer.toString(this.quantity));
            //int index = tCols.indexOf(this);
            //System.out.println(index);
            //tCols.get(index).updatePrice(this.quantity);
            table.refresh();
        }
        
        private void remove(){
            this.quantity -= 1;
            this.quantityLbl.setText(Integer.toString(this.quantity));
           // int index = tCols.indexOf();
            //System.out.println(index);
            //tCols.get(index).updatePrice(this.quantity);
            table.refresh();
        }
        
        public HBox getColDisplay(){
            return colDisplay;
        }
        
        public int getQuantity(){
            return this.quantity;
        }
    }
    
    ObservableList<Wrapper> tCols = FXCollections.observableArrayList();
    private double total = 0;
    private Label totalLbl;
    private DecimalFormat df = new DecimalFormat("#.00");
    
    public class Wrapper{        
        private int itemID;
        private int quantity;
        private Button addBtn = new Button("+");  
        private Button removeBtn = new Button("-");
        private HBox colDisplay = new HBox();
        Label quantityLbl;
        Label priceLbl;
        double originalPrice;
        private String itemName;
        private double price;
        
        public Wrapper(int itemID, String itemName, int q, double price){
            this.itemID = itemID;
            this.itemName = itemName;
            this.quantity = q;
            this.price = price;
            this.originalPrice = price/quantity;
            quantityLbl = new Label(Integer.toString(quantity));
            priceLbl = new Label("$" + df.format(this.price));
            
            colDisplay.setAlignment(Pos.BASELINE_CENTER);
            colDisplay.setSpacing(10);
            colDisplay.getChildren().addAll(removeBtn,quantityLbl,addBtn);
            
            addBtn.setOnAction(e-> {
                try {
                    add();
                } catch (SQLException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            removeBtn.setOnAction(e -> {
                try {
                    remove();
                } catch (SQLException ex) {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            if(this.quantity <= 1)
                this.removeBtn.setDisable(true);
        }
        
        public String getItemName(){
            return this.itemName;
        }
        public HBox getColDisplay(){
            return this.colDisplay;
        }
        
        public double getPrice(){
            return this.price;
        }
        
        public Label getPriceLbl(){
            return this.priceLbl;
        }
        
        public int getQuantity(){
            return this.quantity;
        }
        
        private void add() throws SQLException{
            this.quantity += 1;
            this.quantityLbl.setText(Integer.toString(this.quantity));
            this.price += this.originalPrice;
            this.priceLbl.setText("$" + df.format(this.price));
            total += this.originalPrice;
            totalLbl.setText("Total: $" + df.format(total));
            if (this.quantity > 1)
                this.removeBtn.setDisable(false);
            
            String query = "UPDATE orderitem SET quantity = " + Integer.toString(this.quantity) + " WHERE itemID = " + Integer.toString(this.itemID) +";";
            System.out.println(query);
            s.update(query);
        }
        
        private void remove() throws SQLException{
            this.quantity -= 1;
            this.quantityLbl.setText(Integer.toString(this.quantity));
            this.price -= this.originalPrice;
            this.priceLbl.setText("$" + df.format(this.price));
            total -= this.originalPrice;
            totalLbl.setText("Total: $" + df.format(total));
            if(this.quantity <= 1)
                this.removeBtn.setDisable(true);
            
            String query = "UPDATE orderitem SET quantity = " + Integer.toString(this.quantity) + " WHERE itemID = " + Integer.toString(this.itemID) +";";
            System.out.println(query);
            s.update(query);
        }
        
        public void updatePrice(int quantity){
            if (quantity > this.quantity){
                this.price += originalPrice;
            }
            else if (quantity < this.quantity)
                this.price -= originalPrice;
        }
    }

    SQLConnector s;

    private TableView<Wrapper> table = new TableView();
    
/*
    public static void main(String[] args) {
        launch(args);
    }
*/
    
    public ObservableList<OrderItem> getOrderItems(SQLConnector s) throws SQLException{
        ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
        CachedRowSet results = s.query("SELECT * FROM orderitem");
        
        while(results.next()){
            OrderItem temp = new OrderItem(results.getInt("itemID"), results.getInt("quantity"), s);
            orderItems.add(temp);
        }
        
        return orderItems;
    }
 
    @Override
    public void start(Stage stage) throws SQLException {
   
        //Scene scene = new Scene(new Group());
        //stage.setTitle("Cart");
        //stage.setWidth(640);
        //stage.setHeight(850);
        /*
        //final Label label = new Label("Address Book");
        //label.setFont(new Font("Arial", 20));
 
        table.setEditable(false);
 
        
        CachedRowSet results = s.query("SELECT * FROM orderitem");
        ArrayList<OrderItem> items = new ArrayList<OrderItem>();
        while(results.next()){
            OrderItem temp = new OrderItem(results.getInt("itemID"), results.getInt("quantity"), s);
            items.add(temp);
        }
        
        //public ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
        TableColumn<OrderItem, String> itemCol = new TableColumn<>("Item Name");
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        TableColumn<OrderItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setMinWidth(50);
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        TableColumn<OrderItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(50);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(getOrderItems(s));
        table.getColumns().addAll(itemCol, quantityCol, priceCol);
        
        Button backBtn = new Button("Back");
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
        */
    }
    
    public Scene displayCart() throws SQLException{
 
        //final Label label = new Label("Address Book");
        //label.setFont(new Font("Arial", 20));
 
        table.setEditable(false);
 
        //grabbing the orderitems from the database
        CachedRowSet results = s.query("SELECT * FROM orderitem");
        ArrayList<OrderItem> items = new ArrayList<>();
        while(results.next()){
            OrderItem temp = new OrderItem(results.getInt("itemID"), results.getInt("quantity"), s);
            items.add(temp);
        }
        
        
        for(int i = 0; i < items.size(); i++){
            int itemID = items.get(i).getItemID();
            String itemName = items.get(i).getItemName();
            int quantity = items.get(i).getQuantity();
            double price = items.get(i).getPrice();
            tCols.add(new Wrapper(itemID,itemName,quantity,price));
        }
        
        //defining the columns of the table
        TableColumn<Wrapper, String> itemCol = new TableColumn<>("Item Name");
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        /*
        TableColumn<OrderItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setMinWidth(50);
        quantityCol.setStyle("-fx-alignment: CENTER");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        */
        
        TableColumn<Wrapper, HBox> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setMinWidth(50);
        //quantityCol.setStyle("-fx-alignment: CENTER");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("colDisplay"));
        
        TableColumn<Wrapper, Label> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(50);
        priceCol.setStyle("-fx-alignment: CENTER");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceLbl"));
        
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table.setItems(getOrderItems(s));
        table.setItems(tCols);
        table.getColumns().addAll(itemCol, quantityCol, priceCol);
        
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            //TODO return to home page
        });
        
        /*
        for(int i = 0; i < items.size(); i++){
            total = total + items.get(i).getPrice();
        }
        */
        
        for(Wrapper item : table.getItems())
            total = total + item.getPrice();
        
        String totalOut = "Total: $" + df.format(total);
        totalLbl = new Label(totalOut);
        Button checkoutBtn = new Button("Checkout");
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 10, 10));        
        vbox.getChildren().addAll(table, backBtn);
        
        final VBox bottom = new VBox();
        bottom.setSpacing(5);
        bottom.setAlignment(Pos.BASELINE_RIGHT);
        bottom.setPadding(new Insets(0, 20, 20, 10)); 
        bottom.getChildren().addAll(totalLbl,checkoutBtn);
        
        
        BorderPane cartPane = new BorderPane();
        cartPane.setTop(vbox);
        cartPane.setCenter(table);
        cartPane.setBottom(bottom);
        
        Scene cartScene = new Scene(cartPane, 600,500);
        return cartScene;
    }
    
    public Cart(SQLConnector s){
       this.s = s;
    }
}
