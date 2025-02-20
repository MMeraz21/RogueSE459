package com.group2.rogue.worldgeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.group2.rogue.monsters.Monster;

public class RogueLevel {
    private static final int levelWidth = 80;
    private static final int levelHeight = 24;
    private static final char FLOOR = '.';
    private static final char WALL = '#';
    private static final char EMPTY = ' ';
    private static final char HALLWAY = '+';
    private static final char STAIRS_UP = '%';
    private static final char STAIRS_DOWN = '>';


    private static final int MAX_ROOMS = 8;
    private static final int MIN_ROOM_SIZE = 3;
    private static final int MAX_ROOM_SIZE = 11;

    private static final int GRID_ROWS = 3;
    private static final int GRID_COLS = 3;

    private final Random random = new Random();
    private final char[][] level = new char[levelHeight][levelWidth];
    private final boolean[][] roomExists = new boolean[GRID_ROWS][GRID_COLS];

    private final int[][] centers = new int[9][2];
    private final List<int[]> roomCenters = new ArrayList<>();

    private final boolean[][] connected = new boolean[9][9]; // needed so that we dont get floating rooms  CHANGED
     private final List<Monster> monsters = new ArrayList<>();


    public RogueLevel() {
        generateLevel();
        placeMonsters();
    }

    public void generateLevel() {
        initializeLevel();
        generateRooms();
        connectRooms();
        ensureAllRoomsConnected();
        placeStairs();
    }

    public char[][] getMap() {
        return level;
    }

    public int[] getStartingRoom() {
        if (!roomCenters.isEmpty()) {
            return roomCenters.get(0); // Return the center of the first generated room
        }
        return new int[] { levelWidth / 2, levelHeight / 2 }; // Fallback
    }

    private void initializeLevel() {
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                level[y][x] = EMPTY;
            }
        }
    }

    private void generateRooms() {
        int sectionWidth = levelWidth / GRID_COLS;
        int sectionHeight = levelHeight / GRID_ROWS;

        int maxPossibleWidth = sectionWidth - 4;
        int maxPossibleHeight = sectionHeight - 4;

        int effectiveMaxWidth = Math.min(MAX_ROOM_SIZE, maxPossibleWidth);
        int effectiveMaxHeight = Math.min(MAX_ROOM_SIZE, maxPossibleHeight);

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                if (random.nextDouble() < 0.90) {  // 90% chance to place a room

                    int sectionStartX = col * sectionWidth;  //get the bounds
                    int sectionStartY = row * sectionHeight;

                    int roomWidth = random.nextInt(effectiveMaxWidth - MIN_ROOM_SIZE + 1) + MIN_ROOM_SIZE;   //dimensions of room
                    int roomHeight = random.nextInt(effectiveMaxHeight - MIN_ROOM_SIZE + 1) + MIN_ROOM_SIZE;

                    int maxOffsetX = sectionWidth - roomWidth - 4;
                    int maxOffsetY = sectionHeight - roomHeight - 4;

                    int offsetX = (maxOffsetX > 0) ? random.nextInt(maxOffsetX + 1) : 0;
                    int offsetY = (maxOffsetY > 0) ? random.nextInt(maxOffsetY + 1) : 0;

                    int roomStartX = sectionStartX + 2 + offsetX;
                    int roomStartY = sectionStartY + 2 + offsetY;

                    makeRoom(roomStartX, roomStartY, roomWidth, roomHeight);
                    roomExists[row][col] = true;
                    roomCenters.add(new int[]{roomStartX + roomWidth / 2, roomStartY + roomHeight / 2});
                }
            }
        }
    }

    private void makeRoom(int startX, int startY, int width, int height) {
        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                level[y][x] = FLOOR;
            }
        }

        for (int x = startX - 1; x <= startX + width; x++) {   //two for loops to make the walls, one for horiz, one for vert
            if (x >= 0 && x < levelWidth) {
                if (startY - 1 >= 0) level[startY - 1][x] = WALL;
                if (startY + height < levelHeight) level[startY + height][x] = WALL;
            }
        }

        for (int y = startY - 1; y <= startY + height; y++) {
            if (y >= 0 && y < levelHeight) {
                if (startX - 1 >= 0) level[y][startX - 1] = WALL;
                if (startX + width < levelWidth) level[y][startX + width] = WALL;
            }
        }

        int roomIndex = (startY / (levelHeight / 3)) * 3 + (startX / (levelWidth / 3));   //used to connect the rooms later on
        centers[roomIndex][0] = startX + width / 2;
        centers[roomIndex][1] = startY + height / 2;
    }


    private void connectRooms() {

        boolean[][] isConnected = new boolean[GRID_ROWS][GRID_COLS];

        for (int row = 0; row < GRID_ROWS; row++) {   //horizontal connections
            for (int col = 0; col < GRID_COLS - 1; col++) {
                if (roomExists[row][col] && roomExists[row][col + 1]) {
                    int room1Index = row * 3 + col;
                    int room2Index = row * 3 + (col + 1);
                    createHallway(
                        centers[room1Index][0], centers[room1Index][1],
                        centers[room2Index][0], centers[room2Index][1]
                    );
                    isConnected[row][col] = true;
                    isConnected[row][col + 1] = true;
                }
            }
        }

        for (int col = 0; col < GRID_COLS; col++) {   //vertical connections
            for (int row = 0; row < GRID_ROWS - 1; row++) {
                if (roomExists[row][col] && roomExists[row + 1][col]) {
                    int room1Index = row * 3 + col;
                    int room2Index = (row + 1) * 3 + col;
                    createHallway(
                        centers[room1Index][0], centers[room1Index][1],
                        centers[room2Index][0], centers[room2Index][1]
                    );
                    isConnected[row][col] = true;
                    isConnected[row + 1][col] = true;
                }
            }
        }

        //attempt to connect islands, not working the best, def come back to this later
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                if (roomExists[row][col] && !isConnected[row][col]) {
                    double shortestDist = Double.MAX_VALUE;
                    int closestRow = -1;
                    int closestCol = -1;

                    for (int row2 = 0; row2 < GRID_ROWS; row2++) {   //look for closest room
                        for (int col2 = 0; col2 < GRID_COLS; col2++) {
                            if (roomExists[row2][col2] && isConnected[row2][col2]) {
                                int room1Index = row * 3 + col;
                                int room2Index = row2 * 3 + col2;
                                
                                int x1 = centers[room1Index][0];
                                int y1 = centers[room1Index][1];
                                int x2 = centers[room2Index][0];
                                int y2 = centers[room2Index][1];
                                
                                double dist = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
                                
                                if (dist < shortestDist) {
                                    shortestDist = dist;
                                    closestRow = row2;
                                    closestCol = col2;
                                }
                            }
                        }
                    }

                    if (closestRow != -1) {   //connect isolated room to nearest
                        int room1Index = row * 3 + col;
                        int room2Index = closestRow * 3 + closestCol;
                        createHallway(
                            centers[room1Index][0], centers[room1Index][1],
                            centers[room2Index][0], centers[room2Index][1]
                        );
                        isConnected[row][col] = true;
                    }
                }
            }
        }


    }

    private void ensureAllRoomsConnected() {
        //TODO
    }

    private void createHallway(int x1, int y1, int x2, int y2) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (level[y1][x] == EMPTY || level[y1][x] == WALL) {
                level[y1][x] = HALLWAY;
            }
        }

        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (level[y][x2] == EMPTY || level[y][x2] == WALL) {
                level[y][x2] = HALLWAY;
            }
        }
    }

    private void placeStairs() {
        for (int attempts = 0; attempts < 100; attempts++) {   //100 tries to place both types of stairs, works good enough
            int x = random.nextInt(levelWidth);
            int y = random.nextInt(levelHeight);
            

            if (level[y][x] == FLOOR) {
                level[y][x] = STAIRS_UP;
                break;
            }
        }

        for (int attempts = 0; attempts < 100; attempts++) {
            int x = random.nextInt(levelWidth);
            int y = random.nextInt(levelHeight);

            if (level[y][x] == FLOOR && level[y][x] != STAIRS_UP) {
                level[y][x] = STAIRS_DOWN;
                break;
            }
        }
    }

    public void printLevel() {
        for (char[] row : level) {
            System.out.println(new String(row));
        }
    }

    private void placeMonsters() {
        int monsterCount = random.nextInt(5) + 3;
        for (int i = 0; i < monsterCount; i++) {
            int x, y;
            do {
                x = random.nextInt(levelWidth);
                y = random.nextInt(levelHeight);
            } while (level[y][x] != FLOOR);
            
            monsters.add(Monster.generateRandomMonster(x, y));
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

}
