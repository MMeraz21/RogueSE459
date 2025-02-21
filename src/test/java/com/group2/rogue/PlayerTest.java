package com.group2.rogue;

import com.group2.rogue.worldgeneration.RogueLevel;
import com.group2.rogue.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private RogueLevel dungeon;
    private char[][] map;

    @BeforeEach
    void setUp() {
        dungeon = new RogueLevel(0);
        player = new Player(dungeon);
        map = dungeon.getMap();
    }

    @Test
    void testPlayerStartsOnFloor() {
        assertEquals('.', map[player.getY()][player.getX()], "Player should start on a floor tile.");
    }

    @Test
    void testMoveValidDirection() {
        int startX = player.getX();
        int startY = player.getY();

        player.movePlayer('D'); // Move right
        assertTrue(isFloor(player.getX(), player.getY()), "Player should move right if it's a floor tile.");
        assertNotEquals(startX, player.getX(), "Player X should change when moving right.");
        assertEquals(startY, player.getY(), "Player Y should remain the same when moving right.");
    }

    // @Test
    // void testPreventWallMovement() {
    // int startX = player.getX();
    // int startY = player.getY();

    // // Find the nearest wall and attempt to move into it
    // for (int y = 0; y < map.length; y++) {
    // for (int x = 0; x < map[0].length; x++) {
    // if (map[y][x] == '#') {
    // player.movePlayer(getDirectionTowards(startX, startY, x, y));
    // assertEquals(startX, player.getX(), "Player should not move into a wall.");
    // assertEquals(startY, player.getY(), "Player should not move into a wall.");
    // return;
    // }
    // }
    // }
    // }

    @Test
    void testPreventOutOfBoundsMovement() {
        int startX = player.getX();
        int startY = player.getY();

        player.movePlayer('A'); // Move left
        if (startX == 0) {
            assertEquals(0, player.getX(), "Player should not move out of bounds (left).");
        }

        player.movePlayer('W'); // Move up
        if (startY == 0) {
            assertEquals(0, player.getY(), "Player should not move out of bounds (up).");
        }
    }

    private boolean isFloor(int x, int y) {
        return map[y][x] == '.'; // Check if tile is a floor
    }

    private char getDirectionTowards(int startX, int startY, int targetX, int targetY) {
        if (targetX > startX)
            return 'D';
        if (targetX < startX)
            return 'A';
        if (targetY > startY)
            return 'S';
        return 'W';
    }
}
