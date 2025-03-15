package com.group2.rogue.items;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.group2.rogue.player.Player;

public class Potion extends Item {
    private final PotionType type;
    private final String color;

    private static final List<String> COLORS = List.of(
        "amber", "aquamarine", "black", "blue", "brown", "clear", "crimson", "cyan", "ecru", "gold", "green",
        "grey", "magenta", "orange", "pink", "plaid", "purple", "red", "silver", "tan", "tangerine", "topaz",
        "turquoise", "vermilion", "violet", "white", "yellow"
    );
    private static final Random random = new Random();
    private static final String[] assignedColors = new String[PotionType.values().length];

        static {
        // Shuffle and assign unique colors to each potion type
        List<String> shuffledColors = COLORS.stream().toList();
        Collections.shuffle(shuffledColors, random);
        for (int i = 0; i < PotionType.values().length; i++) {
            assignedColors[i] = shuffledColors.get(i);
        }
    }

    public Potion(PotionType type) {
        super(type.getName());
        this.type = type;
        this.color = assignedColors[type.ordinal()];
    }

    public PotionType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public void applyEffect(Player player) {
        switch (type) {
            case BLINDNESS:  //done, makes player miss attacks for x turns
                player.setBlind(20 + random.nextInt(5) - random.nextInt(5));
                player.showMessage("A cloak of darkness falls around you.");
                break;
            case CONFUSION:   //done, plaeyr moves in a random direction for x turns
                player.setConfused(random.nextInt(8) + 20);
                player.showMessage("Wait, what's going on? Huh? What? Who?");
                break;
            case EXTRA_HEALING:  // done, just affects player stats
                int healAmount = player.getPlayerLevel() * random.nextInt(8);
                player.heal(healAmount);
                player.removeBlindness();
                player.showMessage("You begin to feel much better.");
                break;
            case GAIN_STRENGTH:  // done, just affects player stats
                player.increaseStrength(1);
                player.showMessage("You feel stronger. What bulging muscles!");
                break;
            case HEALING:  // done, just affects player stats
                healAmount = player.getPlayerLevel() * random.nextInt(4);
                player.heal(healAmount);
                player.removeBlindness();
                player.showMessage("You begin to feel better.");
                break;
            case PARALYSIS:  // done, player can't move for x turns
                player.setParalyzed(random.nextInt(4) + 1);
                player.showMessage("You can't move.");
                break;
            case POISON:
                player.decreaseStrength(random.nextInt(3) + 1);
                player.showMessage("You feel very sick.");
                break;
            case RAISE_LEVEL:  // done, just affects player stats
                player.levelUp();
                player.showMessage("You suddenly feel much more skillful.");
                break;
            case RESTORE_STRENGTH:  // done, just affects player stats
                player.restoreStrength();
                player.showMessage("Hey, this tastes great. It makes you feel warm all over.");
                break;
            case THIRST_QUENCHING:
            case OTHER:
                player.showMessage("What an odd tasting potion!");
                break;
        }
    }
    

    @Override
    public String toString() {
        return color + " potion";
    }



}
