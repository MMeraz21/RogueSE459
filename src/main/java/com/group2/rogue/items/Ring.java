package com.group2.rogue.items;

import java.util.Random;

public class Ring extends Item {
    private RingType ringType;
    private String stone;
    private int bonus;
    private static final Random random = new Random();

    private static final String[] STONES = {
        "agate", "alexandrite", "amethyst", "carnelian", "diamond", "emerald", 
        "germanium", "granite", "garnet", "jade", "kryptonite", "lapis lazuli", 
        "moonstone", "obsidian", "onyx", "opal", "pearl", "peridot", "ruby", 
        "sapphire", "stibotantalite", "tiger eye", "topaz", "turquoise", "taaffeite", "zircon"
    };

    public Ring(RingType type) {
        super(generateRingName(type));
        this.ringType = type;
        this.stone = STONES[random.nextInt(STONES.length)];
        
        if (type == RingType.INCREASE_DAMAGE || type == RingType.PROTECTION || type == RingType.ADD_STRENGTH) {
            this.bonus = random.nextInt(2) + 1;
        }
    }

    private static String generateRingName(RingType type) {
        String stone = STONES[random.nextInt(STONES.length)];
        return "ring of " + type.getName() + " (" + stone + ")";
    }

    public RingType getRingType() {
        return ringType;
    }
}
