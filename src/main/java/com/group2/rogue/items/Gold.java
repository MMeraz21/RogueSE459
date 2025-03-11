package com.group2.rogue.items;

public class Gold extends Item {
    private int amount;

    public Gold(int amount) {
        super("Gold");
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Gold: " + amount;
    }
}