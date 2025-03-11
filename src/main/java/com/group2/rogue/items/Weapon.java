package com.group2.rogue.items;

public class Weapon extends Item {
    private int minDamage;
    private int maxDamage;
    private boolean isRanged;
    private String requiredWeapon;

    public Weapon(String name, int minDamage, int maxDamage, boolean isRanged, String requiredWeapon) {
        super(name);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.isRanged = isRanged;
        this.requiredWeapon = requiredWeapon;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public boolean isRanged() {
        return isRanged;
    }

    public String getRequiredWeapon() {
        return requiredWeapon;
    }

    @Override
    public String toString() {
        return getName() + " (Damage: " + minDamage + "-" + maxDamage + ")";
    }
}