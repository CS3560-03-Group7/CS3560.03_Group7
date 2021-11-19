package com.example.ordertime;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Menu {
    private ArrayList<Item> menuList;
    private GridPane visMenu;
    private int rowCount;
    private int columnCount;

    public Menu(){
        menuList = new ArrayList<>();
        visMenu = new GridPane();
        rowCount=0;
        columnCount = 0;
    }
    //This method falls under the Update Menu use case. It adds new items to the menu
    void addToMenu(Item item){
        menuList.add(item);
    }

    //This method falls under the Update Menu use case. It removes items from the menu
    void removeFromMenu(Item item){
        menuList.remove(item);
    }

    public Menu(ArrayList<Item> menuItems) { //This is the constructor for the Menu class
        this.menuList = menuItems;
    }

    //need a method called getItems
    //will help filter items for top navigation buttons
}
