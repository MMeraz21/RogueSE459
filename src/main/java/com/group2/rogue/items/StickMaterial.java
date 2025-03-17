package com.group2.rogue.items;

import java.util.Random;

public enum StickMaterial {
    // Metals
    ALUMINUM("aluminum"),
    BERYLLIUM("beryllium"),
    BONE("bone"),
    BRASS("brass"),
    BRONZE("bronze"),
    COPPER("copper"),
    ELECTRUM("electrum"),
    GOLD("gold"),
    IRON("iron"),
    LEAD("lead"),
    MAGNESIUM("magnesium"),
    MERCURY("mercury"),
    NICKEL("nickel"),
    PEWTER("pewter"),
    PLATINUM("platinum"),
    STEEL("steel"),
    SILVER("silver"),
    SILICON("silicon"),
    TIN("tin"),
    TITANIUM("titanium"),
    TUNGSTEN("tungsten"),
    ZINC("zinc"),
    
    // Woods
    AVOCADO("avocado wood"),
    BALSA("balsa"),
    BAMBOO("bamboo"),
    BANYAN("banyan"),
    BIRCH("birch"),
    CEDAR("cedar"),
    CHERRY("cherry"),
    CINNABAR("cinnabar"),
    CYPRESS("cypress"),
    DOGWOOD("dogwood"),
    DRIFTWOOD("driftwood"),
    EBONY("ebony"),
    ELM("elm"),
    EUCALYPTUS("eucalyptus"),
    FALL("fall"),
    HEMLOCK("hemlock"),
    HOLLY("holly"),
    IRONWOOD("ironwood"),
    KUKUI("kukui wood"),
    MAHOGANY("mahogany"),
    MANZANITA("manzanita"),
    MAPLE("maple"),
    OAKEN("oaken"),
    PERSIMMON("persimmon wood"),
    PECAN("pecan"),
    PINE("pine"),
    POPLAR("poplar"),
    REDWOOD("redwood"),
    ROSEWOOD("rosewood"),
    SPRUCE("spruce"),
    TEAK("teak"),
    WALNUT("walnut"),
    ZEBRAWOOD("zebrawood");

    private final String name;
    private static final Random random = new Random();

    StickMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static StickMaterial getRandomMetal() {
        StickMaterial[] metals = {
            ALUMINUM, BERYLLIUM, BONE, BRASS, BRONZE, COPPER, ELECTRUM, GOLD, 
            IRON, LEAD, MAGNESIUM, MERCURY, NICKEL, PEWTER, PLATINUM, STEEL, 
            SILVER, SILICON, TIN, TITANIUM, TUNGSTEN, ZINC
        };
        return metals[random.nextInt(metals.length)];
    }


    public static StickMaterial getRandomWood() {
        StickMaterial[] woods = {
            AVOCADO, BALSA, BAMBOO, BANYAN, BIRCH, CEDAR, CHERRY, CINNABAR, 
            CYPRESS, DOGWOOD, DRIFTWOOD, EBONY, ELM, EUCALYPTUS, FALL, HEMLOCK, 
            HOLLY, IRONWOOD, KUKUI, MAHOGANY, MANZANITA, MAPLE, OAKEN, PERSIMMON, 
            PECAN, PINE, POPLAR, REDWOOD, ROSEWOOD, SPRUCE, TEAK, WALNUT, ZEBRAWOOD
        };
        return woods[random.nextInt(woods.length)];
    }


    public static StickMaterial getRandomMaterial() {
        if (random.nextBoolean()) {
            return getRandomMetal();
        } else {
            return getRandomWood();
        }
    }
}