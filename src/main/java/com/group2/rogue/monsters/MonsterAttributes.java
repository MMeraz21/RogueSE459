package com.group2.rogue.monsters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterAttributes {
    private static final List<MonsterAttributes> monsters = new ArrayList<>();
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
    private int[] levelsSpawned;
    private static final Random random = new Random();

    static {
        monsters.add(new MonsterAttributes('A', "Aquator", 5, 9, 40, 20, true, false, false, false, false, new int[]{3}));
        monsters.add(new MonsterAttributes('B', "Bat", 1, 8, 8, 1, false, true, false, false, false, new int[]{1, 2, 3, 4, 5, 6}));
        monsters.add(new MonsterAttributes('C', "Centaur", 4, 7, 32, 25, false, false, false, false, false, new int[]{3, 4, 5, 6}));
        monsters.add(new MonsterAttributes('D', "Dragon", 10, 1, 80, 6800, false, false, false, false, false, new int[]{10}));
        monsters.add(new MonsterAttributes('E', "Emu", 1, 10, 8, 2, true, false, false, false, false, new int[]{1, 2, 3, 4}));
        monsters.add(new MonsterAttributes('F', "Venus Flytrap", 8, 3, 64, 80, true, false, false, false, false, new int[]{8}));
        monsters.add(new MonsterAttributes('G', "Griffon", 13, -2, 104, 2000, true, false, true, false, false, new int[]{10}));
        monsters.add(new MonsterAttributes('H', "Hobgoblin", 1, 10, 8, 3, true, false, false, false, false, new int[]{1, 7}));
        monsters.add(new MonsterAttributes('I', "Ice Monster", 1, 10, 8, 15, true, false, false, false, false, new int[]{6, 7, 8}));
        monsters.add(new MonsterAttributes('J', "Jabberwock", 15, -4, 120, 4000, true, false, false, false, false, new int[]{13}));
        monsters.add(new MonsterAttributes('K', "Kestral", 1, 10, 8, 1, false, true, false, false, false, new int[]{1, 4}));
        monsters.add(new MonsterAttributes('L', "Leprechaun", 3, 8, 24, 10, false, false, false, false, true, new int[]{3, 4, 5}));
        monsters.add(new MonsterAttributes('M', "Medusa", 8, 9, 64, 200, true, false, false, false, false, new int[]{9, 10}));
        monsters.add(new MonsterAttributes('N', "Nymph", 3, 3, 24, 37, false, false, false, false, true, new int[]{3, 4, 5, 6}));
        monsters.add(new MonsterAttributes('O', "Orc", 1, 5, 8, 5, true, false, false, false, false, new int[]{1, 6}));
        monsters.add(new MonsterAttributes('P', "Phantom", 8, 8, 64, 120, true, false, false, true, false, new int[]{14}));
        monsters.add(new MonsterAttributes('Q', "Quagga", 3, 9, 24, 32, true, false, false, false, false, new int[]{1, 4}));
        monsters.add(new MonsterAttributes('R', "Rattlesnake", 2, 8, 16, 9, true, false, false, false, false, new int[]{4, 7}));
        monsters.add(new MonsterAttributes('S', "Snake", 2, 3, 16, 1, true, false, false, false, false, new int[]{1, 6}));
        monsters.add(new MonsterAttributes('T', "Troll", 6, 6, 48, 120, true, false, true, false, false, new int[]{6, 7}));
        monsters.add(new MonsterAttributes('U', "Ur-vile", 7, 13, 56, 190, true, false, false, false, false, new int[]{4, 6}));
        monsters.add(new MonsterAttributes('V', "Vampire", 8, 10, 80, 350, true, false, false, false, false, new int[]{14}));
        monsters.add(new MonsterAttributes('W', "Wraith", 5, 7, 40, 55, true, false, false, false, false, new int[]{7}));
        monsters.add(new MonsterAttributes('X', "Xeroc", 7, 4, 56, 100, false, false, false, true, false, new int[]{7}));
        monsters.add(new MonsterAttributes('Y', "Yeti", 4, 6, 32, 50, true, false, false, false, false, new int[]{1, 6}));
        monsters.add(new MonsterAttributes('Z', "Zombie", 2, 3, 16, 6, true, false, false, false, false, new int[]{1, 4}));
    }

    public MonsterAttributes(char symbol, String name, int level, int armor, int health, int experience,
                             boolean isMean, boolean isFlying, boolean isRegenerating, boolean isInvisible, boolean isGreedy, int[] levelsSpawned) {
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
        this.levelsSpawned = levelsSpawned;
    }

    public static MonsterAttributes getRandomMonster(int dungeonLevel) {
        List<MonsterAttributes> validMonsters = new ArrayList<>();
        for (MonsterAttributes monster : monsters) {
            for (int level : monster.levelsSpawned) {
                if (level == dungeonLevel) {
                    validMonsters.add(monster);
                    break;
                }
            }
        }
        return validMonsters.get(random.nextInt(validMonsters.size()));
    }

    public char getSymbol() { return symbol; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getArmor() { return armor; }
    public int getHealth() { return health; }
    public int getExperience() { return experience; }
    public boolean isMean() { return isMean; }
    public boolean isFlying() { return isFlying; }
    public boolean isRegenerating() { return isRegenerating; }
    public boolean isInvisible() { return isInvisible; }
    public boolean isGreedy() { return isGreedy; }
}
