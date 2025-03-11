package com.group2.rogue.items;

public class Armor extends Item {
    private int armorClass;

    public Armor(String name, int armorClass) {
        super(name);
        this.armorClass = armorClass;
    }

    public int getArmorClass() {
        return armorClass;
    }

    @Override
    public String toString() {
        return getName() + " (Armor Class: " + armorClass + ")";
    }
}