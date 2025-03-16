package com.group2.rogue.items;

public class Scroll extends Item {
    private final ScrollType type;



    public Scroll(ScrollType type) {
        super(type.getName());
        this.type = type;
    }

    public ScrollType getType() {
        return type;
    }

    public void applyEffect() {
        switch (type) {
            case CONFUSION:
                System.out.println("You are confused!");
                break;
            case ENCHANT_ARMOR:
                System.out.println("Your armor glows blue for a moment.");
                break;
            case SLEEP:
                System.out.println("You fall asleep.");
                break;
            case TELEPORT:
                System.out.println("You are teleported.");
                break;
            case ENCHANT_WEAPON:
                System.out.println("Your weapon glows blue for a moment.");
                break;
            case NOTHING:
                System.out.println("Nothing happens.");
                break;
            case HOLD_MONSTER:
                System.out.println("You are held by a monster.");
                break;
            case CREATE_MONSTER:
                System.out.println("A monster appears.");
                break;
        }
    }

}
