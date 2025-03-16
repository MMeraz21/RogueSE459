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
    private int minDamage;
    private int maxDamage;
    private int x, y;
    private boolean isConfused = false;
    private int confusionTurns = 0;
    private boolean held = false;
    private int holdTurns = 0;
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

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }
    
    public int getHealth() {
        return health;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isMean() {
        return isMean;
    }
    
    public int getMinDamage() {
        return minDamage;
    }
    
    public int getMaxDamage() {
        return maxDamage;
    }

    public int getLevel() {
        return level;
    }

    public int getArmor() {
        return armor;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isConfused() {
        return isConfused;
    }

    public int getConfusionTurns() {
        return confusionTurns;
    }

    public void updateConfusion() {
        confusionTurns--;
        if (confusionTurns == 0) {
            isConfused = false;
        }
    }

    public void setConfused(boolean confused) {
        isConfused = confused;
    }

    public void setConfusionTurns(int turns) {
        confusionTurns = turns;
    }

    public boolean isHeld() {
        return held;
    }

    public void setHeld(boolean held) {
        this.held = held;
    }

    public void setHoldTurns(int turns) {
        holdTurns = turns;
    }

}
