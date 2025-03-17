package com.group2.rogue.items;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.jline.utils.NonBlockingReader;

import com.group2.rogue.monsters.Monster;
import com.group2.rogue.monsters.MonsterAttributes;
import com.group2.rogue.player.Player;
import com.group2.rogue.worldgeneration.World;

public class Stick extends Item {
    private final StickType type;
    private final StickMaterial material;
    private int charges;
    private final Random random = new Random();
    

    public Stick(StickType type, StickMaterial material) {
        super(createName(type, material));
        this.type = type;
        this.material = material;

        switch (type) {
            case LIGHT:
                this.charges = random.nextInt(10) + 1 + 10; // 1d10 + 10
                break;
            default:
                this.charges = random.nextInt(5) + 1 + 3; // 1d5 + 3
                break;
        }
    }

    private static String createName(StickType type, StickMaterial material) {
        return "Stick of " + type.getName() + " (" + material.getName() + ")";
    }

    public StickType getType() {
        return type;
    }

    public StickMaterial getMaterial() {
        return material;
    }

    public int getCharges() {
        return charges;
    }

    public boolean hasCharges() {
        return charges > 0;
    }

    public void useCharge() {
        if (charges > 0) {
            charges--;
        }
    }

    public void zap(Player player, World world, NonBlockingReader reader) {
        if (!hasCharges()) {
            System.out.println("The " + getName() + " doesn't seem to have any power left.");
            return;
        }

        // Use a charge
        useCharge();

        // Apply effect based on stick type
        switch (type) {
            case POLYMORPH:
                polymorphMonster(getTargetMonster(player, world, reader), world);
                break;
            case LIGHT:
                applyLight(player, world);
                break;
            case LIGHTNING:
                shootLightning(getTargetMonster(player, world, reader), player, world);
                break;
            case MAGIC_MISSILE:
                shootMagicMissile(getTargetMonster(player, world, reader), world);
                break;
            case TELEPORT_AWAY:
                teleportAway(getTargetMonster(player, world, reader), world);
                break;
            case FIRE:
                shootFireBolt(getTargetMonster(player, world, reader), world);
                break;
            case STRIKING:
                strikeMonster(getTargetMonster(player, world, reader));
                break;
            case DRAIN_LIFE:
                drainLife(player, world);
                break;
        }
    }

    private Monster getTargetMonster(Player player, World world, NonBlockingReader reader) {
        if (type == StickType.DRAIN_LIFE || type == StickType.LIGHT) {
            return null;
        }
        
        System.out.println("Select a direction to zap (use WASD keys):");
        
        try {
            int input = reader.read();
            if (input == -1) return null;
            
            char key = Character.toUpperCase((char) input);
            
            int targetX = player.getX();
            int targetY = player.getY();
            
            switch (key) {
                case 'W': targetY--; break; // Up
                case 'A': targetX--; break; // Left
                case 'S': targetY++; break; // Down
                case 'D': targetX++; break; // Right
                default: 
                    System.out.println("Invalid direction.");
                    return null;
            }
            
            Monster target = world.getFirstMonsterInDirection(player.getX(), player.getY(), targetX, targetY);
            
            if (target == null) {
                System.out.println("There's no monster in that direction.");
            }
            
            return target;
            
        } catch (IOException e) {
            System.out.println("Error reading input.");
            return null;
        }
    }

    private void polymorphMonster(Monster target, World world) {
        if (target == null) return;
        
        System.out.println("The " + target.getName() + " shimmers and changes form!");
        
        // generate a random monster
        Monster newMonster = Monster.generateRandomMonster(target.getX(), target.getY(), world.getDungeonLevel() + 1);
        
        //remove current target and add the new monster
        world.removeMonster(target);
        world.addMonster(newMonster);
        
        System.out.println("It is now a " + newMonster.getName() + "!");
    }

    private void applyLight(Player player, World world) {
        if (player.isBlind()) {
            player.removeBlindness();
            System.out.println("The room is filled with light.");
        } else {
            System.out.println("The room is already well-lit.");
        }
    }

    private void shootLightning(Monster target, Player player, World world) {
        if (target == null) return;
        
        System.out.println("A bolt of lightning shoots from the " + getName() + "!");
        
        int damage = random.nextInt(8) + 1;
        
        if (random.nextInt(20) + 1 > 5) { // 75% chance to hit
            target.takeDamage(damage);
            System.out.println("The lightning bolt hits the " + target.getName() + " for " + damage + " damage!");
            
            if (target.getHealth() <= 0) {
                System.out.println("The " + target.getName() + " is killed by the lightning!");
                world.removeMonster(target);
            }
        } else {
            System.out.println("The lightning bolt misses the " + target.getName() + "!");
        }
    }

    private void shootMagicMissile(Monster target, World world) {
        if (target == null) return;
        
        System.out.println("A magic missile shoots from the " + getName() + "!");
        
        int damage = random.nextInt(8) + 1;
        
        target.takeDamage(damage);
        System.out.println("The missile hits the " + target.getName() + " for " + damage + " damage!");
        
        if (target.getHealth() <= 0) {
            System.out.println("The " + target.getName() + " is killed by the missile!");
            world.removeMonster(target);
        } else {
            System.out.println("The " + target.getName() + " has " + target.getHealth() + " health remaining.");
        }
    }

    private void teleportAway(Monster target, World world) {
        if (target == null) return;
        
        System.out.println("The " + target.getName() + " vanishes!");
        
        int[] newPosition = world.findRandomEmptyPosition();  //find random empty position
        int newX = newPosition[0];
        int newY = newPosition[1];
        
        target.setPosition(newX, newY);
    }

    private void shootFireBolt(Monster target, World world) {
        if (target == null) return;
        
        System.out.println("A bolt of fire shoots from the " + getName() + "!");
        
        if (target.getName().toLowerCase().contains("dragon")) {  //draogons immune to fireball
            System.out.println("The " + target.getName() + " seems immune to fire!");
            return;
        }
        
        int damage = 0;
        for (int i = 0; i < 6; i++) {
            damage += random.nextInt(6) + 1;
        }
        
        target.takeDamage(damage);
        System.out.println("The fire bolt hits the " + target.getName() + " for " + damage + " damage!");
        
        if (target.getHealth() <= 0) {
            System.out.println("The " + target.getName() + " is incinerated!");
            world.removeMonster(target);
        } else {
            System.out.println("The " + target.getName() + " has " + target.getHealth() + " health remaining.");
        }
    }

    private void strikeMonster(Monster target) {
        if (target == null) return;
        
        System.out.println("An invisible force strikes from the " + getName() + "!");
        
        int attackRoll = random.nextInt(20) + 1 + 4;
        int monsterAC = target.getArmor();
        
        if (attackRoll >= monsterAC) {
            int damage = random.nextInt(8) + 1 + random.nextInt(8) + 1;
            
            // 5% chance for 3d8 damage
            if (random.nextInt(100) < 5) {
                damage += random.nextInt(8) + 1;
                System.out.println("Critical hit!");
            }
            
            target.takeDamage(damage);
            System.out.println("The force strikes the " + target.getName() + " for " + damage + " damage!");
            
            if (target.getHealth() <= 0) {
                System.out.println("The " + target.getName() + " is killed by the force!");
            } else {
                System.out.println("The " + target.getName() + " has " + target.getHealth() + " health remaining.");
            }
        } else {
            System.out.println("The force misses the " + target.getName() + "!");
        }
    }

    private void drainLife(Player player, World world) {
        if (player.getHits() < 2) {
            System.out.println("You don't have enough life force to drain!");
            return;
        }
        
        int drainAmount = player.getHits() / 2;
        player.takeDamage(drainAmount);
        
        System.out.println("You feel your life force draining!");
        
        List<Monster> allMonsters = world.getAllMonsters();
        
        if (allMonsters.isEmpty()) {
            System.out.println("There are no monsters to drain life from.");
            return;
        }
        
        int damagePerMonster = drainAmount / allMonsters.size();
        
        if (damagePerMonster <= 0) {
            damagePerMonster = 1; // Minimum damage
        }
        
        int monstersKilled = 0;
        for (Monster monster : allMonsters) {
            monster.takeDamage(damagePerMonster);
            
            if (monster.getHealth() <= 0) {
                monstersKilled++;
                world.removeMonster(monster);
            }
        }
        

        
        System.out.println("Your life force damages all monsters for " + damagePerMonster + " points each!");
        System.out.println(monstersKilled + " monsters were killed by the drain!");
    }

}
