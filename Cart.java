package com.example.ordertime;

import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Cart {
    private ArrayList<Item> cart;
    private int itemsInCart;
    private double subtotal;

    public Cart(){
        cart.add(new Item());
        itemsInCart = 0;
        subtotal = 0.0;
    }

    public Label addToCart(Item item){
        cart.add(item);
        String iPrice = String.format("%.2f", item.getPrice());
        Label cartItem = new Label(item.getItemName() + " " + iPrice );

        return cartItem;
    }

    public int getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public boolean clearCart(){
        this.itemsInCart = 0;
        for(int i =0; i<cart.size(); i++){
            cart.remove(i);
        }
        return true;
    }
}
