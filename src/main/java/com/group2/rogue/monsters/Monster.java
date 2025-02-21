package com.group2.rogue.monsters;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Monster {
    private char symbol;
    private String name;
    private int level;
    private int armor;
    private int health;
    private int experience;
    private boolean isMean;
    private boolean isFlying;
    private boolean isRegenerating;
    private boolean isInvisible;
    private boolean isGreedy;
    private int x, y;
    private static final Random random = new Random();

    public Monster(char symbol, String name, int level, int armor, int health, int experience,
                    boolean isMean, boolean isFlying, boolean isRegenerating, boolean isInvisible, boolean isGreedy, int x, int y) {
        this.symbol = symbol;
        this.name = name;
        this.level = level;
        this.armor = armor;
        this.health = health;
        this.experience = experience;
        this.isMean = isMean;
        this.isFlying = isFlying;
        this.isRegenerating = isRegenerating;
        this.isInvisible = isInvisible;
        this.isGreedy = isGreedy;
        this.x = x;
        this.y = y;
    }

    public static Monster generateRandomMonster(int x, int y, int dungeonLevel) {
        MonsterAttributes attributes = MonsterAttributes.getRandomMonster(dungeonLevel);
        return new Monster(attributes.getSymbol(), attributes.getName(), attributes.getLevel(), attributes.getArmor(),
                attributes.getHealth(), attributes.getExperience(), attributes.isMean(), attributes.isFlying(),
                attributes.isRegenerating(), attributes.isInvisible(), attributes.isGreedy(), x, y);
    }

    public char getSymbol() {
        return symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
