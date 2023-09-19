package com.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vending_machine")
public class VendingMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "vending_machine_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "exist")
    private Boolean exist;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "vending_machine_coin",
            joinColumns = {@JoinColumn(name = "vending_machine_id")},
            inverseJoinColumns = {@JoinColumn(name = "coin_id")}
    )
    List<Coin> coins = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "vending_machine_product",
            joinColumns = {@JoinColumn(name = "vending_machine_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    List<Product> products = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }
}
