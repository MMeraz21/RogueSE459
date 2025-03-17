package com.group2.rogue.items;

public enum StickType {
    POLYMORPH("Polymorph"),
    LIGHT("Light"),
    LIGHTNING("Lightning"),
    MAGIC_MISSILE("Magic Missile"),
    TELEPORT_AWAY("Teleport Away"),
    FIRE("Fire"),
    STRIKING("Striking"),
    DRAIN_LIFE("Drain Life");

    private final String name;

    StickType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}