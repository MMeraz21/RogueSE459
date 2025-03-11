package com.group2.rogue.items;

public class Food extends Item {
    private int nutrition;

    public Food(String name, int nutrition) {
        super(name);
        this.nutrition = nutrition;
    }

    public int getNutrition() {
        return nutrition;
    }

    @Override
    public String toString() {
        return getName() + " (Nutrition: " + nutrition + ")";
    }
}