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


public class CS3560KioskProject extends Application {
    Stage window;
    Scene updateMenuScene, addItemScene;
    Connection conn = null;
    PreparedStatement ps;
    TableView<Product> table;
    TextField itemIdTF;
    TextField itemNameTF;
    TextField isAvailTF;
    TextField itemPriceTF;
    TextField itemPicTF;
    Button addItemBtn = new Button();
    Button returnBtn = new Button();
    int ind;
    int oldItemID;
    
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
        
        TableColumn<Product, Integer> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setMinWidth(50);
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        
        TableColumn<Product, String> itemCol = new TableColumn<>("Item");
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        TableColumn<Product, Integer> isAvailCol = new TableColumn<>("Available");
        isAvailCol.setMinWidth(50);
        isAvailCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(50);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableView<Product> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(getProduct());
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

        Button saveItem = new Button("Save item");
        saveItem.setOnMouseClicked(event -> {
            if (itemIdTF.getText() != null || itemNameTF.getText() != null
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
                itemNameTF.setText("");
                isAvailTF.setText("");
                itemPriceTF.setText("");
            }
        });

        returnBtn = new Button("Return");
        returnBtn.setOnAction(e -> {
            window.setTitle("UPDATE MENU");
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
        updateItem.setOnMouseClicked(event -> {
            if (itemIdTF != null || itemNameTF != null
                    || isAvailTF != null || itemPriceTF != null) {
                try {
                    conn = MySqlConnection();
                    
                    
                    ps = conn.prepareStatement("update item set "
                            + "itemID = ?, itemName = ?, isAvailable = ?, price = ? where itemID = " + oldItemID);
                    ps.setInt(1, Integer.parseInt(itemIdTF.getText()));
                    ps.setString(2, itemNameTF.getText());
                    ps.setInt(3, Integer.parseInt(isAvailTF.getText()));
                    ps.setDouble(4, Double.parseDouble(itemPriceTF.getText()));
                    
                    int res = ps.executeUpdate();
                    if (res >= 1) {
                        System.out.println("Item Successfully Updated");
                    } else {
                        System.out.println("Item Failed to Update");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CS3560KioskProject.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        showItemToFields(ind);

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
    
    public void showItemToFields(int index) {
        itemIdTF.setText(Integer.toString(getProduct().get(index).getItemID()));
        itemNameTF.setText(getProduct().get(index).getItemName());
        isAvailTF.setText(Integer.toString(getProduct().get(index).getIsAvailable()));
        itemPriceTF.setText(Double.toString(getProduct().get(index).getPrice()));
        
        
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
 
    public ObservableList<Product> getProduct() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        try {
            conn = MySqlConnection();
            ResultSet rs = conn.createStatement().executeQuery("select * from item");
            while (rs.next()) {
                products.add(new Product(rs.getInt(1), rs.getString("itemName"), 
                        rs.getInt(3), Double.parseDouble(rs.getString(4))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CS3560KioskProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return products;
    }

    public static void main(String[] args) {
        launch(args);
    }
     
}
