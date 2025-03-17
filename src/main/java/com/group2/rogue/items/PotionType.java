package com.group2.rogue.items;

public enum PotionType {
    BLINDNESS("Blindness"),
    CONFUSION("Confusion"),
    EXTRA_HEALING("Extra Healing"),
    GAIN_STRENGTH("Gain Strength"),
    HEALING("Healing"),
    PARALYSIS("Paralysis"),
    POISON("Poison"),
    RAISE_LEVEL("Raise Level"),
    RESTORE_STRENGTH("Restore Strength"),
    THIRST_QUENCHING("Thirst Quenching"),
    OTHER("Other");

    private final String name;

    PotionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

