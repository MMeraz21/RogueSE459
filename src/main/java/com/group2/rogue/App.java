package com.group2.rogue;

import com.group2.rogue.worldgeneration.World;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        World world = new World();
        world.generateWorld();
        world.placePlayer();

        System.out.println("Use W A S D to move. Press 'Q' to quit.");

        try (Terminal terminal = TerminalBuilder.builder().system(true).jna(true).build();
             NonBlockingReader reader = terminal.reader()) {
            
            terminal.enterRawMode(); // for raw mode, allows for instant key presses

            while (true) {
                world.displayWorld();
                System.out.print("Move (WASD) | Quit (Q): ");
                terminal.flush();

                int input = reader.read();
                if (input == -1) continue; // no input

                char key = Character.toUpperCase((char) input);

                if (key == 'Q') {
                    System.out.println("\nExiting...");
                    break;
                } else if  (key == 'P') {
                    world.getPlayer().drinkPotion(reader);
                } else if (key == 'E') {
                    world.getPlayer().eatFood();
                } else if (key == 'R') {
                    world.getPlayer().cycleWeapons();
                } else if (key == 'T') {
                    world.getPlayer().cycleArmor();
                } else {
                    world.movePlayer(reader, key);
                }

                //world.movePlayer(reader, key);
                System.out.println(); // important for formatting
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
