package main;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Josh
 */
public class Welcome extends Application {
/*
    @Override
    public void start(Stage stage) throws Exception {
        String url = "jdbc:mysql://localhost:3306/cs3560f21";
        String username = "root";
        String password = "CS3560@";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

        SQLConnector s = new SQLConnector(url,username,password);

        Label welcomeLbl = new Label("Welcome! Press \"Get Started\" to begin your order!");
        welcomeLbl.setFont(new Font("Arial",24));
        welcomeLbl.setTextAlignment(TextAlignment.CENTER);

        Button btn1 = new Button("Get Started");
        EventHandler<ActionEvent> click;
        click = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {
                LocalDateTime orderLDT = LocalDateTime.now();
                String orderStartTime = dateFormat.format(orderLDT);
                String query = "INSERT INTO cart(startTime) VALUES \"" + orderStartTime + "\";";
                try {
                    s.update(query);
                } catch (SQLException ex) {
                    Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
                }
                stage.setTitle("Main Menu");
                //stage.setScene(Main.getHomePage());

            }
        };
        btn1.setOnAction(click);
        StackPane root = new StackPane();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        content.getChildren().addAll(welcomeLbl,btn1);
        root.getChildren().add(content);
        Scene scene = new Scene(root, 600,400);
        stage.setScene(scene);
        stage.setTitle("Welcome");
        stage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
    */

    @Override
    public void start(Stage stage) throws Exception {
        Scene helloWorld = goHome(stage);
        stage.setScene(helloWorld);
        stage.setTitle("Welcome");
        stage.show();

    }

    public static Scene goHome(Stage stage){
        String url = "jdbc:mysql://localhost:3306/cs3560f21";
        String username = "root";
        String password = "CS3560@";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
        SQLConnector s = new SQLConnector(url,username,password);

        Label welcomeLbl = new Label("Welcome!\n Press \"Get Started\" to begin your order!");
        welcomeLbl.getStyleClass().add("label-Subtitle");
        welcomeLbl.setTextAlignment(TextAlignment.CENTER);

        Button btn1 = new Button("Get Started");
        btn1.getStyleClass().add("button-completeOrder");
        EventHandler<ActionEvent> click;
        click = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {
                try {
                    String query = "INSERT INTO cart(startTime) VALUES(\"" + dateFormat.format(LocalDateTime.now()) + "\");";
                    s.update(query);
                    //Cart shoppingCart = new Cart(s);
                    stage.setTitle("Main Menu");
                    MainMenu mainMenu = new MainMenu(s);
                    stage.setScene(mainMenu.getHomePage(stage));
                } catch (SQLException ex) {
                    Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        btn1.setOnAction(click);
        StackPane root = new StackPane();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        content.getChildren().addAll(welcomeLbl,btn1);
        root.getChildren().add(content);
        Scene scene = new Scene(root, 600,500);
        scene.getStylesheets().add("file:///C:/Users/Josh/Desktop/styles.css");

        return scene;
    }

    public static void main(String[] args){
        launch(args);
    }
}
