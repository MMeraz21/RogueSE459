package com.group2.rogue.player;

import com.group2.rogue.worldgeneration.RogueLevel;

import java.io.IOException;


// private int level;

public class Player {
    private int x, y;
    private char[][] dungeonMap;
    private static final char PLAYER_ICON = '@';
    private static final char STAIRS_DOWN = '>';
    private static final char STAIRS_UP = '%'; 


    //player stats
    private int level = 1;  //this is the current level of the world 
    private int playerLevel = 1; //this is the level of the player, not to be confused with the above variable
    private int hits = 12;  //the number of health points the player has, if this number reaches 0 the player dies
    private int strength = 16;  // strength of the player, influences how much damage they do
    private int gold = 0;
    private int armor = 5; 
    private int experience = 0;

    public Player(RogueLevel dungeon) {
        this.dungeonMap = dungeon.getMap();
        int[] startingRoom = dungeon.getStartingRoom();

        if (startingRoom != null) {
            this.x = startingRoom[0];
            this.y = startingRoom[1];
        }


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

    private boolean isWalkable(int newX, int newY) {
        if (newX < 0 || newY < 0 || newX >= dungeonMap[0].length || newY >= dungeonMap.length) {
            return false;
        }
        char tile = dungeonMap[newY][newX];
        return tile == '.' || tile == '+' || tile == '>' || tile == '%'; // can now move on floor, hallways, stairs up and down
    }


    private void printPosition() {
        System.out.println("Player is at (" + x + ", " + y + ")");
    }

    public String getStats() {
        return String.format("Level: %d  Gold: %d  Hp: %d(%d)  Str: %d  Armor: %d  Exp: %d/%d",
            level, gold, hits, hits, strength, armor, playerLevel, experience);
    }
    
    public int getLevelIndex() { return level; }

    public int levelIndexDown() {
        return level--;
    }

    public int levelIndexUp() {
        return level++;
    }

    public void setLevel(RogueLevel dungeon) {
        this.dungeonMap = dungeon.getMap();
        int[] startingRoom = dungeon.getStartingRoom();
        if (startingRoom != null) {
            setPosition(startingRoom[0], startingRoom[1]);
        }
    }

    // New method to set position
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() { return x; }
    public int getY() { return y; }

    public void takeDamage(int damage) {
        hits = Math.max(0, hits - damage);
    }
    
    public void addExperience(int exp) {
        experience += exp;
        // add level up logic
    }
    
    public int getHits() {
        return hits;
    }
}
