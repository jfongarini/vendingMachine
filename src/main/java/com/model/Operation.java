package com.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "operation_id")
    private int operationId;

    @Column(name = "value")
    private Double value;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "vending_machine_id", nullable = false)
    private VendingMachine vendingMachine;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "operation_coin",
            joinColumns = {@JoinColumn(name = "operation_id")},
            inverseJoinColumns = {@JoinColumn(name = "coin_id")}
    )
    List<Coin> coins = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "operation_product",
            joinColumns = {@JoinColumn(name = "operation_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    List<Product> products = new ArrayList<>();

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public VendingMachine getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
