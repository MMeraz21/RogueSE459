package com.group2.rogue.player;

import com.group2.rogue.items.Item;
import com.group2.rogue.items.ItemGenerator;
import com.group2.rogue.items.Weapon;
import com.group2.rogue.worldgeneration.RogueLevel;
import com.group2.rogue.items.Armor;
import com.group2.rogue.items.Food;
import com.group2.rogue.items.Gold;
import com.group2.rogue.worldgeneration.World;
import com.group2.rogue.items.Potion;
import com.group2.rogue.items.PotionType;
import com.group2.rogue.items.Ring;
import com.group2.rogue.items.RingType;
import com.group2.rogue.items.Scroll;
import com.group2.rogue.items.ScrollType;
import com.group2.rogue.items.Stick;
import com.group2.rogue.items.StickMaterial;
import com.group2.rogue.items.StickType;

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
    private boolean isSleeping = false;
    private int sleepTurnsLeft = 0;

    private Ring ring1;
    private Ring ring2;

    private World world;

    public Player(RogueLevel dungeon, World world) {
        initializeInventory();
        this.dungeonMap = dungeon.getMap();
        this.world = world;
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

        //add two scrolls to inventory
        inventory.add(new Scroll(ScrollType.TELEPORT));
        inventory.add(new Scroll(ScrollType.SLEEP));

        //add two sticks to inventory
        inventory.add(new Stick(StickType.POLYMORPH, StickMaterial.ALUMINUM));
        inventory.add(new Stick(StickType.DRAIN_LIFE, StickMaterial.BANYAN));

        //add two rings to inventory
        inventory.add(new Ring(RingType.TELEPORTATION));
        inventory.add(new Ring(RingType.INCREASE_DAMAGE));

        //add two potions to inventory
        inventory.add(new Potion(PotionType.PARALYSIS));
        inventory.add(new Potion(PotionType.RAISE_LEVEL));

        //add an extra armor and weapon
        inventory.add(new Armor("Chain Mail", 5));
        inventory.add(new Weapon("Two-Handed Sword", 4, 4, false, null));
        
        
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

            if (item instanceof Gold) {
                Gold goldItem = (Gold) item;
                gold += goldItem.getAmount();
                System.out.println("You picked up " + goldItem.getAmount() + " gold.");
                return;
            }
            
            inventory.add(item);
            System.out.println("You picked up: " + item);
        } else {
            System.out.println("Your pack is full!");
        }
    }

    public String getStats() {
        return String.format("Level: %d  Gold: %d  Hp: %d(%d)  Str: %d  Armor: %d  Exp: %d/%d",
            level, gold, hits, hits, strength, getArmor(), playerLevel, experience);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Ring getRing1() {
        return ring1;
    }
    public Ring getRing2() {
        return ring2;
    }

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
        if(this.ring1.getRingType() == RingType.SUSTAIN_STRENGTH || this.ring2.getRingType() == RingType.SUSTAIN_STRENGTH) {
            System.out.println("Your ring protects your strength from being lowered");
            return;
        }
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

    public void setSleeping(boolean bool) {
        isSleeping = bool;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void updateSleep() {
        if (isSleeping) {
            sleepTurnsLeft--;
            if (sleepTurnsLeft <= 0) {
                isSleeping = false;
            }
        }
    }

    public void setSleepTurns(int turns) {
        sleepTurnsLeft = turns;
    }

    public int getSleepTurns() {
        return sleepTurnsLeft;
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
        List<Item> scrolls = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Scroll) {
                scrolls.add(item);
            }
        }
        
        if (scrolls.isEmpty()) {
            System.out.println("You don't have any scrolls.");
            return;
        }
        
        System.out.println("\nAvailable Scrolls:");
        for (int i = 0; i < scrolls.size(); i++) {
            System.out.println((i + 1) + ": " + scrolls.get(i).getName());
        }
        
        System.out.println("Select a scroll to use (1-" + scrolls.size() + ") or 0 to cancel: ");
        
        try {
            int selection = -1;
            while (selection < 0 || selection > scrolls.size()) {
                int input = reader.read();
                if (input == -1) continue;
                
                char key = (char) input;
                if (Character.isDigit(key)) {
                    selection = Character.getNumericValue(key);
                    
                    if (selection < 0 || selection > scrolls.size()) {
                        System.out.println("Invalid selection. Try again (0-" + scrolls.size() + "): ");
                    }
                }
            }
            
            if (selection == 0) {
                System.out.println("Cancelled.");
                return;
            }
            
            Scroll selectedScroll = (Scroll) scrolls.get(selection - 1);
            
            System.out.println("\nYou read the " + selectedScroll.getName() + ".");
            selectedScroll.applyEffect(this, world, reader);
            
            inventory.remove(selectedScroll);
            
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }


    public void useStick(NonBlockingReader reader) {
        List<Item> sticks = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Stick) {
                sticks.add(item);
            }
        }
        
        if (sticks.isEmpty()) {
            System.out.println("You don't have any sticks.");
            return;
        }
        
        System.out.println("\nAvailable Sticks:");
        for (int i = 0; i < sticks.size(); i++) {
            System.out.println((i + 1) + ": " + sticks.get(i).getName());
        }
        
        System.out.println("Select a stick to use (1-" + sticks.size() + ") or 0 to cancel: ");
        
        try {
            int selection = -1;
            while (selection < 0 || selection > sticks.size()) {
                int input = reader.read();
                if (input == -1) continue;
                
                char key = (char) input;
                if (Character.isDigit(key)) {
                    selection = Character.getNumericValue(key);
                    
                    if (selection < 0 || selection > sticks.size()) {
                        System.out.println("Invalid selection. Try again (0-" + sticks.size() + "): ");
                    }
                }
            }
            
            if (selection == 0) {
                System.out.println("Cancelled.");
                return;
            }
            
            Stick selectedStick = (Stick) sticks.get(selection - 1);
            
            System.out.println("\nYou zapped the " + selectedStick.getName() + ".");
            selectedStick.zap(this, world, reader);

            if (selectedStick.getCharges() == 0) {
                inventory.remove(selectedStick);
            }
            
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    public void useRing(NonBlockingReader reader) {
        List<Item> rings = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Ring) {
                rings.add(item);
            }
        }
        
        if (rings.isEmpty()) {
            System.out.println("You don't have any rings.");
            return;
        }
        
        System.out.println("\nAvailable Rings:");
        for (int i = 0; i < rings.size(); i++) {
            System.out.println((i + 1) + ": " + rings.get(i).getName());
        }
        
        System.out.println("Select a ring to use (1-" + rings.size() + ") or 0 to cancel: ");
        
        try {
            int selection = -1;
            while (selection < 0 || selection > rings.size()) {
                int input = reader.read();
                if (input == -1) continue;
                
                char key = (char) input;
                if (Character.isDigit(key)) {
                    selection = Character.getNumericValue(key);
                    
                    if (selection < 0 || selection > rings.size()) {
                        System.out.println("Invalid selection. Try again (0-" + rings.size() + "): ");
                    }
                }
            }
            
            if (selection == 0) {
                System.out.println("Cancelled.");
                return;
            }
            
            Ring selectedRing = (Ring) rings.get(selection - 1);
            
            System.out.println("\nYou put on the " + selectedRing.getName() + ".");
            equipRing(selectedRing);

            if (selectedRing.getRingType() == RingType.TELEPORTATION) {
                teleportPlayer(this, world);
            }
            //selectedRing.applyEffect(this, world, reader);
            
            inventory.remove(selectedRing);
            
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    public void equipRing(Ring ring) {
        if (ring1 == null) {
            ring1 = ring;
            System.out.println("You equipped: " + ring);
        } else if (ring2 == null) {
            ring2 = ring;
            System.out.println("You equipped: " + ring);
        } else {
            System.out.println("You already have two rings equipped. Remove one to equip another.");
        }
    }

    private void teleportPlayer(Player player, World world) {
        int[] randomLocation = world.findRandomEmptyPosition();
        int newX = randomLocation[0];
        int newY = randomLocation[1];

        player.setPosition(newX, newY);
        System.out.println("You find yourself in a different location");
    }

}