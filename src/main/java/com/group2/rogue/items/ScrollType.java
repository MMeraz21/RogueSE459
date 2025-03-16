package com.group2.rogue.items;

public enum ScrollType {
    CONFUSION("Confusion"),
    ENCHANT_ARMOR("Enchant Armor"),
    SLEEP("Sleep"),
    TELEPORT("Teleport"),
    ENCHANT_WEAPON("Enchant Weapon"),
    NOTHING("Nothing"),
    HOLD_MONSTER("Hold Monster"),
    CREATE_MONSTER("Create Monster");

    private final String name;

    ScrollType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
