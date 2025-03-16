package com.group2.rogue.player;

import com.group2.rogue.items.Item;
import com.group2.rogue.items.Weapon;
import com.group2.rogue.worldgeneration.RogueLevel;
import com.group2.rogue.items.Armor;
import com.group2.rogue.items.Food;
import com.group2.rogue.worldgeneration.World;
import com.group2.rogue.items.Potion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jline.utils.NonBlocking;
import org.jline.utils.NonBlockingReader;

public class Player {
    private int x, y;
    private char[][] dungeonMap;
    private static final char PLAYER_ICON = '@';
    private static final char STAIRS_DOWN = '>';
    private static final char STAIRS_UP = '%';

    // Player stats
    private int level = 1;  // Current dungeon level
    private int playerLevel = 1; // Player's experience level
    private int hits = 12;  // Health points
    private int strength = 16;  // Strength
    private int gold = 0;
    private int armor = 5;
    private int experience = 0;
    private int experienceToNextLevel = 10;

    private Weapon weapon;
    private Armor equippedArmor;

    // private int foodSupply = 10;
    private boolean isFainted = false;
    private int faintTurnsLeft = 0;

    // Player inventory
    private List<Item> inventory = new ArrayList<>();
    private static final int MAX_INVENTORY_SIZE = 23;

    // Variables for potion effects
    private boolean isBlind = false;
    private int blindTurnsLeft = 0;
    private boolean isConfused = false;
    private int confusedTurnsLeft = 0;
    private boolean isParalyzed = false;
    private int paralysisTurnsLeft = 0;

    public Player(RogueLevel dungeon) {
        initializeInventory();
        this.dungeonMap = dungeon.getMap();
        int[] startingRoom = dungeon.getStartingRoom();

        if (startingRoom != null) {
            this.x = startingRoom[0];
            this.y = startingRoom[1];
        }
    }

    private void initializeInventory() {
        inventory.add(new Food("Ration", 1000));

        Armor startingArmor = new Armor("Leather", 8);
        inventory.add(startingArmor);
        equippedArmor = startingArmor;

        Weapon startingWeapon = new Weapon("Mace", 2, 4, false, null);
        inventory.add(startingWeapon);
        weapon = startingWeapon;
        
    }


    public void movePlayer(char direction) {
        int newX = x, newY = y;

        switch (direction) {
            case 'W': newY--; break;
            case 'S': newY++; break;
            case 'A': newX--; break;
            case 'D': newX++; break;
            default: return;
        }

        if (isWalkable(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public boolean isWalkable(int newX, int newY) {
        if (newX < 0 || newY < 0 || newX >= dungeonMap[0].length || newY >= dungeonMap.length) {
            return false;
        }
        char tile = dungeonMap[newY][newX];
        return tile == '.' || tile == '+' || tile == '>' || tile == '%'; // Can move on floor, hallways, stairs up and down
    }

    public void pickUpItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item);
            System.out.println("You picked up: " + item);
        } else {
            System.out.println("Your pack is full!");
        }
    }

    public String getStats() {
        return String.format("Level: %d  Gold: %d  Hp: %d(%d)  Str: %d  Armor: %d  Exp: %d/%d",
            level, gold, hits, hits, strength, armor, playerLevel, experience);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void takeDamage(int damage) {
        hits = Math.max(0, hits - damage);
    }

    public void addExperience(int exp) {
        experience += exp;
        // Add level-up logic
        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    public void levelUp() {
        playerLevel++;
        experienceToNextLevel *= 2;
    }

    public int getHits() {
        return hits;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getStrength() {
        return strength + weapon.getDamage();
    }

    public int getArmor() {
        return armor + equippedArmor.getArmorClass();
    }

    public void setLevel(RogueLevel dungeon) {
        this.dungeonMap = dungeon.getMap();
        int[] startingRoom = dungeon.getStartingRoom();
        if (startingRoom != null) {
            setPosition(startingRoom[0], startingRoom[1]);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int levelIndexDown() {
        return level--;
    }

    public int levelIndexUp() {
        return level++;
    }

    public void eatFood() {
        for (Item item : inventory) {
            if (item instanceof Food) {
                Food food = (Food) item;
                System.out.println("You ate " + food.getName() + ". It will sustain you for " + food.getNutrition() + " turns.");
                inventory.remove(food);
                return;
            }
        }
        System.out.println("You have no food left!");
    }


    public void faint(int turns) {
        isFainted = true;
        faintTurnsLeft = turns;
    }

    public void reduceFaintTime() {
        if (isFainted) {
            faintTurnsLeft--;
            if (faintTurnsLeft <= 0) {
                isFainted = false;
            }
        }
    }

    public boolean isFainted() {
        return isFainted;
    }

    public void cycleWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Weapon) {
                weapons.add((Weapon) item);
            }
        }

        if(weapons.isEmpty()) {
            System.out.println("You have no weapons to equip.");
            return;
        }

        int currentWeaponIndex = weapons.indexOf(weapon);
        int nextWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
        weapon = weapons.get(nextWeaponIndex);
        System.out.println("You equipped: " + weapon);
    }

    public void cycleArmor() {
        List<Armor> armors = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Armor) {
                armors.add((Armor) item);
            }
        }

        if(armors.isEmpty()) {
            System.out.println("You have no armor to equip.");
            return;
        }

        int currentArmorIndex = armors.indexOf(equippedArmor);
        int nextArmorIndex = (currentArmorIndex + 1) % armors.size();
        equippedArmor = armors.get(nextArmorIndex);
        System.out.println("You equipped: " + equippedArmor);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public void heal(int amount) {
        hits = Math.min(hits + amount, hits);
    }

    public void increaseStrength(int amount) {
        strength += amount;
    }

    public void decreaseStrength(int amount) {
        strength -= amount;
    }

    public void restoreStrength() {
        strength = 16;
    }

    public void setBlind(int turns) {
        isBlind = true;
        blindTurnsLeft = turns;
    }

    public void removeBlindness() {
        isBlind = false;
    }
    public boolean isBlind() {
        return isBlind;
    }

    public void updateBlindness() {
        if (isBlind) {
            blindTurnsLeft--;
            if (blindTurnsLeft <= 0) {
                isBlind = false;
            }
        }
    }

    public void setConfused(int turns) {
        isConfused = true;
        confusedTurnsLeft = turns;
    }

    public boolean isConfused() {
        return isConfused;
    }

    public void updateConfusion() {
        if (isConfused) {
            confusedTurnsLeft--;
            if (confusedTurnsLeft <= 0) {
                isConfused = false;
            }
        }
    }

    public void setParalyzed(int turns) {
        isParalyzed = true;
        paralysisTurnsLeft = turns;
    }

    public boolean isParalyzed() {
        return isParalyzed;
    }

    public void updateParalysis() {
        if (isParalyzed) {
            paralysisTurnsLeft--;
            if (paralysisTurnsLeft <= 0) {
                isParalyzed = false;
            }
        }
    }

    public void showMessage(String message) {
        World.messages.add(message);
    }

    public void drinkPotion(NonBlockingReader reader) {
        List<Potion> potions = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Potion) {
                potions.add((Potion)item);
            }
        }

        if(potions.isEmpty()) {
            System.out.println("You have no potions to drink.");
            return;
        }

        System.out.println("Choose a potion to drink:");
        for (int i = 0; i < potions.size(); i++) {
            Potion potion = potions.get(i);
            System.out.println((i + 1) + ". " + potion);
        }

        int choice = -1;
        System.out.print("> ");
        try {
            int input = reader.read();
            if (input == -1) {
                System.out.println("Invalid input.");
                return;
            }

            char inputChar = (char) input;
            if (Character.isDigit(inputChar)) {
                choice = Character.getNumericValue(inputChar) - 1;
            }

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
            return;
        }

        if (choice < 0 || choice >= potions.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Potion chosenPotion = potions.get(choice);
        chosenPotion.applyEffect(this);

        inventory.remove(chosenPotion);
        System.out.println("You drank the " + chosenPotion + " and it has been consumed.");


    }

    public void useScroll(NonBlockingReader reader) {
        
    }
}