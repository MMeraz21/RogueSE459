package com.group2.rogue.worldgeneration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jline.utils.NonBlockingReader;

import com.group2.rogue.monsters.Monster;
import com.group2.rogue.player.Player;

public class World {
    private List <RogueLevel> levels;
    private RogueLevel currLevel;  //points to the current level
    private Player player;

    public void generateWorld() {
        levels = new ArrayList<>();   //world has 9 levels
        for (int i = 0; i < 9; i++) {
            levels.add(new RogueLevel());
        }
        currLevel = levels.get(0);
    }

    public void placePlayer() {
        player = new Player(currLevel);
    }

    public void movePlayer(NonBlockingReader reader, char direction) {
        player.movePlayer(direction);
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
    


    public void displayWorld() {
        char[][] map = currLevel.getMap();
        List<Monster> monsters = currLevel.getMonsters();
        int playerX = player.getX();
        int playerY = player.getY();
    
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
                    if (x == playerX && y == playerY) {
                        System.out.print('@'); // Player icon
                    } else {
                        System.out.print(map[y][x]);
                    }
                }
            }
            System.out.println();
        }
    
        // Print player stats at the end
        System.out.println(player.getStats());
    }
    
}
