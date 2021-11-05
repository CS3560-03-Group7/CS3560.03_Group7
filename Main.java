package com.example.ordertime;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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

    @Override
    public void start(Stage primaryStage) {

        //for the nuggies; set to be eventually placed in a gridpane or something for future
        Button btnAddToOrder = new Button("Add to Order");
        btnAddToOrder.setPrefWidth(200);
        nuggies.setFitWidth(150);
        nuggies.setFitHeight(150);
        nugLabel.getStyleClass().add(".label-white");
        VBox vNugs = new VBox(nuggies, nugLabel);
        vNugs.setAlignment(Pos.CENTER);
        vNugs.setSpacing(15);

        //for the pretty patties ~~ i mean borgs
        burger.setFitWidth(150);
        burger.setFitHeight(150);
        burgLabel.getStyleClass().add(".label-white");
        VBox vBurg = new VBox(burger, burgLabel);
        vBurg.setAlignment(Pos.CENTER);
        vBurg.setSpacing(15);

        //for the fritas
        fries.setFitWidth(150);
        fries.setFitHeight(150);
        fryLabel.getStyleClass().add(".label-white");
        VBox vFry = new VBox(fries, fryLabel);
        vFry.setAlignment(Pos.CENTER);
        vFry.setSpacing(15);

        //for the lunchables
        kidsMeal.setFitWidth(150);
        kidsMeal.setFitHeight(150);
        kMealLabel.getStyleClass().add(".label-white");
        VBox vKMeal = new VBox(kidsMeal, kMealLabel);
        vKMeal.setAlignment(Pos.CENTER);
        vKMeal.setSpacing(15);

        //for the boys in my yard
        milkShake.setFitWidth(150);
        milkShake.setFitHeight(150);
        mShakeLabel.getStyleClass().add(".label-white");
        VBox vShake = new VBox(milkShake, mShakeLabel);
        vShake.setAlignment(Pos.CENTER);
        vShake.setSpacing(15);

        //for the rootbeer floats
        rootbeerFloat.setFitWidth(150);
        rootbeerFloat.setFitHeight(150);
        rbFloatLabel.getStyleClass().add(".label-white");
        VBox vFloat = new VBox(rootbeerFloat, rbFloatLabel);
        vFloat.setAlignment(Pos.CENTER);
        vFloat.setSpacing(15);

        //for all your menu needs
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

        Scene scene = new Scene(menu);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Menu");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
