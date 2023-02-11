package com.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "coin")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private int coinId;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Double value;

    @ManyToMany(mappedBy = "coins")
    private List<VendingMachine> vendingMachines = new ArrayList<>();

    @ManyToMany(mappedBy = "coins")
    private List<Operation> operations = new ArrayList<>();

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<VendingMachine> getVendingMachines() {
        return vendingMachines;
    }

    public void setVendingMachines(List<VendingMachine> vendingMachines) {
        this.vendingMachines = vendingMachines;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
