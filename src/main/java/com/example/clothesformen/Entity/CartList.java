package com.example.clothesformen.Entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class CartList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartID;
    private Long productID;
    private int quantity;

    private double price;
    private boolean checkout;


    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isCheckout() {
        return checkout;
    }

    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
