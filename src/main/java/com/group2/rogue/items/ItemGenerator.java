package com.group2.rogue.items;

import java.util.Random;

public class ItemGenerator {
    private static final Random random = new Random();

    // Weapons table (from the document)
    private static final Weapon[] WEAPONS = {
        new Weapon("Mace", 2, 4, false, null),
        new Weapon("Longsword", 3, 4, false, null),
        new Weapon("Bow", 1, 1, true, null),
        new Weapon("Arrow", 1, 1, true, "Bow"),
        new Weapon("Dagger", 1, 6, false, null),
        new Weapon("Two-Handed Sword", 4, 4, false, null),
        new Weapon("Dart", 1, 1, true, null),
        new Weapon("Crossbow", 1, 1, true, null),
        new Weapon("Crossbow Bolt", 1, 2, true, "Crossbow"),
        new Weapon("Spear", 2, 3, false, null)
    };

    // Armor table (from the document)
    private static final Armor[] ARMOR = {
        new Armor("Leather", 8),
        new Armor("Studded Leather", 7),
        new Armor("Ring Mail", 7),
        new Armor("Scale Mail", 6),
        new Armor("Chain Mail", 5),
        new Armor("Banded Mail", 4),
        new Armor("Splint Mail", 4),
        new Armor("Plate Mail", 3)
    };

    // Food table (from the document)
    private static final Food[] FOOD = {
        new Food("Ration", 1000), // Represents 1000 turns of food
        new Food("Apple", 500),
        new Food("Bread", 800)
    };

    // Generate a random weapon
    public static Weapon generateWeapon() {
        return WEAPONS[random.nextInt(WEAPONS.length)];
    }

    // Generate a random armor
    public static Armor generateArmor() {
        return ARMOR[random.nextInt(ARMOR.length)];
    }

    // Generate a random food item
    public static Food generateFood() {
        return FOOD[random.nextInt(FOOD.length)];
    }

    // Generate a random amount of gold (based on dungeon level)
    public static Gold generateGold(int dungeonLevel) {
        int minGold = 2;
        int maxGold = 50 + 10 * dungeonLevel;
        int amount = minGold + random.nextInt(maxGold - minGold + 1);
        return new Gold(amount);
    }
}