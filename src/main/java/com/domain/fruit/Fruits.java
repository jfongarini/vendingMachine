package com.domain.fruit;

import java.util.ArrayList;
import java.util.List;

public class Fruits {
    private List<Fruit> fruits = new ArrayList<>();

    public List<Fruit> getFruits() {
        return fruits;
    }

    public void setFruits(List<Fruit> fruits) {
        this.fruits = fruits;
    }
}
