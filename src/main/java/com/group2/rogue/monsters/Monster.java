package com.group2.rogue.monsters;

import java.util.Random;

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

    public static Monster generateRandomMonster(int x, int y) {
        char[] symbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] names = {"Aquator", "Bat", "Centaur", "Dragon", "Emu", "Flytrap", "Griffon", "Hobgoblin", "Ice Monster", "Jabberwock",
                "Kestral", "Leprechaun", "Medusa", "Nymph", "Orc", "Phantom", "Quagga", "Rattlesnake", "Snake", "Troll",
                "Ur-vile", "Vampire", "Wraith", "Xeroc", "Yeti", "Zombie"};
        int index = random.nextInt(symbols.length);

        return new Monster(symbols[index], names[index], random.nextInt(10) + 1, random.nextInt(10),
                (random.nextInt(8) + 1) * (random.nextInt(3) + 1), random.nextInt(200),
                random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), x, y);
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
