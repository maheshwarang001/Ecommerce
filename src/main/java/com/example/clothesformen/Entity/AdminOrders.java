package com.example.clothesformen.Entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class AdminOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderid;

    @ElementCollection
    private List<Long> productid;

    private String email;

    @ElementCollection
    private List<String> productnames;

    private String name;

    private String address;

    private Double totalpurchase;

    private LocalDate date;

    private String creditcard;



    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public List<Long> getProductid() {
        return productid;
    }

    public void setProductid(List<Long> productid) {
        this.productid = productid;
    }

    public List<String> getProductnames() {
        return productnames;
    }

    public void setProductnames(List<String> productnames) {
        this.productnames = productnames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalpurchase() {
        return totalpurchase;
    }

    public void setTotalpurchase(Double totalpurchase) {
        this.totalpurchase = totalpurchase;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
