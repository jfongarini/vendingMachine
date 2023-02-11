package com.domain.fruit;

public class Nutritions {

    private Double carbohydrates;
    private Double protein;
    private Double fat;
    private int calories;
    private Double sugar;

    public Nutritions() {
    }

    public Nutritions(Double carbohydrates, Double protein, Double fat, int calories, Double sugar) {
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fat = fat;
        this.calories = calories;
        this.sugar = sugar;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }
}
