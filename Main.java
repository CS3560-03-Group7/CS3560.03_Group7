package com.example.ordertime;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Main extends Application {
    private Image dinoNugs = new Image(getClass().getResource("dinoNuggiesAreCool.jpg").toExternalForm());
    private Image borgor = new Image(getClass().getResource("borgor.jpg").toExternalForm());
    private Image fritas = new Image(getClass().getResource("fritasYum.jpg").toExternalForm());
    private Image kidsMealsy = new Image(getClass().getResource("kidsMealOWO.jpeg").toExternalForm());
    private Image milkyShakey = new Image(getClass().getResource("milkyshakey.jpg").toExternalForm());
    private Image rootBeerFloat = new Image(getClass().getResource("rootbeerFloatin.jpg").toExternalForm());

    private ImageView nuggies = new ImageView(dinoNugs);
    private ImageView burger = new ImageView(borgor);
    private ImageView fries = new ImageView(fritas);
    private ImageView kidsMeal = new ImageView(kidsMealsy);
    private ImageView milkShake = new ImageView(milkyShakey);
    private ImageView rootbeerFloat = new ImageView(rootBeerFloat);

    private Label nugLabel = new Label("Dino Nuggets");
    private Label burgLabel = new Label("Burger");
    private Label fryLabel = new Label("Fries");
    private Label kMealLabel = new Label("Kids Meal - Chicken Nuggets");
    private Label mShakeLabel = new Label("Milk Shake");
    private Label rbFloatLabel = new Label("Rootbeer Float");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button btnAddToOrder = new Button("Add to Order");
        btnAddToOrder.setPrefWidth(200);
        VBox vNugs = new VBox(nuggies, nugLabel, btnAddToOrder);
        vNugs.setSpacing(15);

        Scene scene = new Scene(vNugs);

        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
