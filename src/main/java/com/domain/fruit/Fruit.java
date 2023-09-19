package com.domain.fruit;

public class Fruit {
    private String genus;
    private String name;
    private int id;
    private String family;
    private String order;
    private Nutritions nutritions;

    public Fruit(String genus, String name, int id, String family, String order, Nutritions nutritions) {
        this.genus = genus;
        this.name = name;
        this.id = id;
        this.family = family;
        this.order = order;
        this.nutritions = nutritions;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Nutritions getNutritions() {
        return nutritions;
    }

    public void setNutritions(Nutritions nutritions) {
        this.nutritions = nutritions;
    }
}

