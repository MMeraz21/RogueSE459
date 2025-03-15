package com.group2.rogue.worldgeneration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.jline.utils.NonBlockingReader;

import com.group2.rogue.items.Armor;
import com.group2.rogue.items.Food;
import com.group2.rogue.items.Gold;
import com.group2.rogue.items.Item;
import com.group2.rogue.items.Weapon;
import com.group2.rogue.monsters.Monster;
import com.group2.rogue.player.Player;

public class World {
    private List <RogueLevel> levels;
    private RogueLevel currLevel;  //points to the current level
    private Player player;

    private int turn = 0;
    private int nextMealTurn;
    private static final int MEAL_INTERVAL = 1300;
    private static final int MEAL_VARIANCE = 130;
    private static final int STARVATION_LIMIT = 850;

    public static List <String> messages = new ArrayList<>();

    public void generateWorld() {
        levels = new ArrayList<>();   // world has 9 levels
        for (int i = 0; i < 9; i++) {
            levels.add(new RogueLevel(i + 1)); // Pass level index starting from 1
        }
        currLevel = levels.get(0);
    }

    public Player getPlayer() {
        return player;
    }

    public void placePlayer() {
        player = new Player(currLevel);
        initializeHunger();
    }

    public void movePlayer(NonBlockingReader reader, char direction) {
        if (player.isFainted()) {
            player.reduceFaintTime();
            messages.add("You are unconscious and cannot move.");
            return;
        }

        if(player.isParalyzed()) {
            messages.add("You are paralyzed and cannot move.");
            player.updateParalysis();
            return;
        }

        turn++;
        updateHunger();

        if (player.isConfused()) {  // player is confused, move in random direction (overrides user input)
            char[] directions = {'W', 'A', 'S', 'D'};
            direction = directions[new Random().nextInt(directions.length)];
            messages.add("You feel dizzy and move unpredictably!");
        }

        int oldX = player.getX();
        int oldY = player.getY();
    
        // Calculate attempted position
        int attemptedX = oldX;
        int attemptedY = oldY;
        switch (direction) {
            case 'W': attemptedY--; break;
            case 'S': attemptedY++; break;
            case 'A': attemptedX--; break;
            case 'D': attemptedX++; break;
        }
    
        // Check for monsters
        Monster monster = getMonsterAt(attemptedX, attemptedY);
        if (monster != null) {
            initiateCombat(reader, monster);
            return;
        }
    
        // Check for items
        Item item = currLevel.getItemAt(attemptedX, attemptedY);
        if (item != null) {
            player.pickUpItem(item);
            currLevel.removeItem(attemptedX, attemptedY);
            messages.add("You picked up: " + item);
        }
    
        // Move the player
        if (isWalkable(attemptedX, attemptedY)) {
            player.movePlayer(direction);
        }
    
        // Check for stairs
        char tile = currLevel.getMap()[player.getY()][player.getX()];
        if (tile == '>') {
            if (promptMove(reader, "Are you sure you want to go down to the next level? (y/n): ")) {
                moveToNextLevel();
            }
        } else if (tile == '%') {
            if (promptMove(reader, "Are you sure you want to go up to the previous level? (y/n): ")) {
                moveToPreviousLevel();
            }
        }

        player.updateConfusion();
    }

    private boolean isWalkable(int x, int y) {
        return player.isWalkable(x, y);
    }
    

    public void moveToNextLevel() {
        int currentLevelIndex = levels.indexOf(currLevel);
        if (currentLevelIndex < levels.size() - 1) {
            currLevel = levels.get(currentLevelIndex + 1);
            // needed to make change so that new player isnt created when moving to next level
            player.setLevel(currLevel);
            player.levelIndexUp();
            int[] startingRoom = currLevel.getStartingRoom();
            if (startingRoom != null) {
                player.setPosition(startingRoom[0], startingRoom[1]);
            }
            System.out.println("Moving to the next level...");
        }
    }

    public void moveToPreviousLevel() {
        int currentLevelIndex = levels.indexOf(currLevel);
        if (currentLevelIndex > 0) {
            currLevel = levels.get(currentLevelIndex - 1);
            // needed to make change so that new player isnt created when moving to previous level
            player.setLevel(currLevel);
            player.levelIndexDown();
            int[] startingRoom = currLevel.getStartingRoom();
            if (startingRoom != null) {
                player.setPosition(startingRoom[0], startingRoom[1]);
            }
            System.out.println("Moving to the previous level...");
        }
    }

    private boolean promptMove(NonBlockingReader reader, String message) {
        System.out.print(message);
        System.out.flush();

        try {
            while (true) {
                int input = reader.read(); // reads a single character from the nonblocking reader we use in app.java
                if (input == -1) continue; // No input, keep waiting

                char key = Character.toLowerCase((char) input);
                if (key == 'y') return true;  // yes, move
                if (key == 'n') return false; // no, dont move
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // so that player doesnt move when theres an error
        }
    }

    private Monster getMonsterAt(int x, int y) {
        for (Monster monster : currLevel.getMonsters()) {
            if (monster.getX() == x && monster.getY() == y) {
                return monster;
            }
        }
        return null;
    }

    private void initiateCombat(NonBlockingReader reader, Monster monster) {
        playerAttack(monster);
        displayWorld();
        waitForSpace(reader);  // Wait for space before showing monster's attack
        
        if (monster.getHealth() > 0) {  // Only do monster attack if it's still alive
            monsterAttack(monster);
            displayWorld();
            waitForSpace(reader);
            
            if (monster.getHealth() <= 0) {
                handleMonsterDeath(monster);
                displayWorld();
                waitForSpace(reader);
            }
        } else {
            handleMonsterDeath(monster);
            displayWorld();
            waitForSpace(reader);
        }
    }

    private void waitForSpace(NonBlockingReader reader) {
        try {
            while (true) {
                int input = reader.read();
                if (input == -1) continue;
                if ((char)input == ' ') break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playerAttack(Monster monster) {
        if (player.isBlind()) {  //blindness state
            if (Math.random() < 0.5) {  // 50% chance to miss when blind
                messages.add("You missed your attack due to blindness!");
                player.updateBlindness();
                return;
            }
        }

        int roll = rollDice(10, 20);  //changed min for pres
        int strengthMod = calculateStrengthAttackModifier(player.getStrength());
        int totalRoll = roll + player.getPlayerLevel() + strengthMod;
        
        // roll needs to beat 20 - attacker's level - defender's armor class
        int toHit = 20 - player.getPlayerLevel() - monster.getArmor();
        
        if (totalRoll >= toHit) {  // successful hit
            int damageBonus = calculateStrengthDamageModifier(player.getStrength());
            int damage = Math.max(1, damageBonus + rollDice(8, 10)); // we haven't added weapons yet, MAKE SURE TO CHANGE THIS, THIS IS FOR DEMO PURPOSES
            monster.takeDamage(damage);
            // System.out.println("You hit the " + monster.getName() + " for " + damage + " damage!");
            String message = "You hit the " + monster.getName() + " for " + damage + " damage!";
            messages.add(message);
        } else {
            String message = "You miss the " + monster.getName() + "!";
            messages.add(message);
            //System.out.println("You miss the " + monster.getName() + "!");
        }
    }

    private void monsterAttack(Monster monster) {
        int roll = rollDice(1, 20);
        int totalRoll = roll + monster.getLevel();
        
        int toHit = 20 - monster.getLevel() - player.getArmor();
        
        if (totalRoll >= toHit) {
            int damage = rollDice(monster.getMinDamage(), monster.getMaxDamage());
            player.takeDamage(damage);
            //System.out.println("The " + monster.getName() + " hits you for " + damage + " damage!");
            String message = "The " + monster.getName() + " hits you for " + damage + " damage!";
            messages.add(message);
        } else {
            //System.out.println("The " + monster.getName() + " misses you!");
            String message = "The " + monster.getName() + " misses you!";
            messages.add(message);
        }
    }
    
    private int rollDice(int min, int max) {
        return min + new Random().nextInt(max - min + 1);
    }

    private void handleMonsterDeath(Monster monster) {
        // System.out.println("You have slain the " + monster.getName() + "!");
        String message = "You have slain the " + monster.getName() + "!";
        messages.add(message);
        player.addExperience(monster.getExperience());
        currLevel.removeMonster(monster);
    }

    private int calculateStrengthAttackModifier(int strength) {
        if (strength < 8) return -7;
        if (strength < 17) return -4;
        if (strength < 19) return -3;
        if (strength < 21) return -2;
        if (strength < 31) return -1;
        return 4; // Maximum attack bonus
    }
    
    private int calculateStrengthDamageModifier(int strength) {
        if (strength < 8) return -7;
        if (strength < 16) return -6;
        if (strength < 17) return -5;
        if (strength < 18) return -4;
        if (strength < 20) return -3;
        if (strength < 22) return -2;
        if (strength < 31) return -1;
        return 6; // Maximum damage bonus
    }

    private void clearMessages() {
        messages.clear();
    }

    private void initializeHunger() {
        nextMealTurn = turn + MEAL_INTERVAL + new Random().nextInt(2 * MEAL_VARIANCE) - MEAL_VARIANCE;
    }

    private void updateHunger() {
        int turnsSinceLastMeal = turn - nextMealTurn;

        if (turnsSinceLastMeal == -300) {
            messages.add("You are starting to get hungry.");
        } else if (turnsSinceLastMeal == -150) {
            messages.add("You are starting to feel weak.");
        } else if (turnsSinceLastMeal >= 0 && turnsSinceLastMeal < STARVATION_LIMIT) {
            messages.add("You feel very weak. You faint from the lack of food.");
            player.faint(rollDice(1, 8) + 4);
        } else if (turnsSinceLastMeal >= STARVATION_LIMIT) {
            messages.add("You have died from starvation.");
            //implement death later
            //player.die();
        }
    }



    public void displayWorld() {
        char[][] map = currLevel.getMap();
        List<Monster> monsters = currLevel.getMonsters();
        Map<Position, Item> items = currLevel.getItems(); // Use Position as keys
        int playerX = player.getX();
        int playerY = player.getY();
    
        for (String message : messages) {
            System.out.println(message);
        }
        clearMessages();
    
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                boolean isMonster = false;
                for (Monster monster : monsters) {
                    if (monster.getX() == x && monster.getY() == y) {
                        System.out.print(monster.getSymbol());
                        isMonster = true;
                        break;
                    }
                }
    
                if (!isMonster) {
                    // Check if there's an item at this position
                    Item item = currLevel.getItemAt(x, y);
                    if (item != null) {
                        if (item instanceof Food) {
                            System.out.print('f'); // Food
                        } else if (item instanceof Weapon) {
                            System.out.print(')'); // Weapon
                        } else if (item instanceof Armor) {
                            System.out.print(']'); // Armor
                        } else if (item instanceof Gold) {
                            System.out.print('$'); // Gold
                        }
                    } else if (x == playerX && y == playerY) {
                        System.out.print('@'); // Player
                    } else {
                        System.out.print(map[y][x]); // Default map tile
                    }
                }
            }
            System.out.println();
        }
    
        // Print player stats at the end
        System.out.println(player.getStats());
    }
}