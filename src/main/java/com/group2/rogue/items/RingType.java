package com.group2.rogue.items;

public enum RingType {
    SUSTAIN_STRENGTH("Sustain Strength"),
    INCREASE_DAMAGE("Increase Damage"),
    STEALTH("Stealth"),
    PROTECTION("Protection"),
    ADD_STRENGTH("Add Strength"),
    TELEPORTATION("Teleportation");
    
    private final String name;
    
    RingType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static RingType getRandomRingType() {
        RingType[] types = RingType.values();
        return types[new java.util.Random().nextInt(types.length)];
    }
}