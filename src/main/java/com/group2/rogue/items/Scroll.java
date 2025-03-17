package com.group2.rogue.items;

import java.util.List;
import java.util.Random;

import com.group2.rogue.monsters.Monster;
import com.group2.rogue.player.Player;
import com.group2.rogue.worldgeneration.World;
import org.jline.utils.NonBlockingReader;

public class Scroll extends Item {
    private final ScrollType type;
    private final Random random = new Random();



    public Scroll(ScrollType type) {
        super(type.getName());
        this.type = type;
    }

    public ScrollType getType() {
        return type;
    }

    public void applyEffect(Player player, World world, NonBlockingReader reader) {
        switch (type) {
            case CONFUSION:  //make it so that monsters that are confused only have  50% chance to attack DONE
                confuseMonsters(player, world);
                break;
            case ENCHANT_ARMOR://done, just updates stats 
                enchantArmor(player);
                break;
            case SLEEP:  //done, player cannot move for x turns
                putPlayerToSleep(player);
                break;
            case TELEPORT:  //done, moves player to a random location
                teleportPlayer(player, world);
                break;
            case ENCHANT_WEAPON: //done, just updates stats
                enchantWeapon(player);
                break;
            case NOTHING:  //done, nothing happens
                System.out.println("Nothing happens.");
                break;
            case HOLD_MONSTER:
                holdMonsters(player, world);
                break;
            case CREATE_MONSTER:  //done, creates a monster
                createMonster(world);
                break;
        }
    }

    private void confuseMonsters(Player player, World world) {
        int playerX = player.getX();
        int playerY = player.getY();

        List<Monster> nearbyMonsters = world.getMonstersWithinRange(playerX, playerY, 2);

        if (nearbyMonsters.isEmpty()) {
            System.out.println("Nothing happens.");
            return;
        } else {
            System.out.println(nearbyMonsters.size() + " monster(s) become confused.");
        }

        int confusionDuration = random.nextInt(8) + 1 + 20;

        for (Monster monster : nearbyMonsters) {
            monster.setConfused(true);
            monster.setConfusionTurns(confusionDuration);
        }

    }

    private void enchantArmor(Player player) {
        Armor currentArmor = player.getEquippedArmor();

        if (currentArmor != null) {
            currentArmor.setArmorClass(currentArmor.getArmorClass() + 1);
        } else {
            System.out.println("You feel like you should be wearing armor.");
        }
    }

    private void putPlayerToSleep(Player player) {
        int sleepDuration = random.nextInt(5) + 5;
        player.setSleeping(true);
        player.setSleepTurns(sleepDuration);
        System.out.println("You fall asleep.");
    }

    private void teleportPlayer(Player player, World world) {
        int[] randomLocation = world.findRandomEmptyPosition();
        int newX = randomLocation[0];
        int newY = randomLocation[1];

        player.setPosition(newX, newY);
        System.out.println("You find yourself in a different location");
    }

    private void enchantWeapon(Player player) {
        Weapon currentWeapon = player.getWeapon();

        if (currentWeapon != null) {
            currentWeapon.setDamage(currentWeapon.getMinDamage() + 1, currentWeapon.getMaxDamage() + 1);
            System.out.println("Your " + currentWeapon.getName() + " seems sharper now.");
        } else {
            System.out.println("You feel like you should be holding a weapon.");
        }
    }

    private void holdMonsters(Player player, World world) {
        int playerX = player.getX();
        int playerY = player.getY();

        List<Monster> nearbyMonsters = world.getMonstersWithinRange(playerX, playerY, 2);

        if (nearbyMonsters.isEmpty()) {
            System.out.println("Nothing happens.");
            return;
        } else {
            System.out.println(nearbyMonsters.size() + " monster(s) freeze in place.");
        }

        if (nearbyMonsters.isEmpty()) {
            System.out.println("Nothing happens.");
            return;
        } else {
            System.out.println(nearbyMonsters.size() + " monster(s) become held.");
        }

        int holdDuration = random.nextInt(4) + 1;

        for (Monster monster : nearbyMonsters) {
            monster.setHeld(true);
            monster.setHoldTurns(holdDuration);
        }
    }

    private void createMonster(World world) {
        int[] randomLocation = world.findRandomEmptyPosition();
        int newX = randomLocation[0];
        int newY = randomLocation[1];

        Monster monster = Monster.generateRandomMonster(newX, newY, world.getDungeonLevel());
        world.addMonster(monster);
        System.out.println("A monster appears!");
    }

}
