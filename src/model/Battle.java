package model;

//************************************************
//Brennon Ary
//ITEC 3860
//11/11/2025
//Combat script
//This function is called if the player enters a space with a monster.
//************************************************

import java.util.Scanner;

public class Battle {

    public static void startBattle(Player player, DatabaseManager.MonsterData monsterData, Scanner scanner) {
        String monsterID = "M" + monsterData.id;
        String name = monsterData.name;
        int hp = monsterData.hp;
        int attackMin = monsterData.attackMin;
        int attackMax = monsterData.attackMax;
        int defense = monsterData.defense;
        String description = "A hostile creature.";
        String location = "R" + player.getRoom();
        String drops = null;

        Monster monster = new Monster(monsterID, name, description, hp, attackMin, attackMax, defense, location, drops);

        System.out.println("\n=== BATTLE START ===");
        System.out.println("Enemy: " + monster.getName());
        System.out.println("Your HP: " + player.getHealth() + "/100   Enemy HP: " + monster.getHealth() + "/" + monster.getMaxHealth());
        System.out.println();

        battle(player, monster, scanner);

        if (player.getHealth() > 0 && monster.getHealth() <= 0) {
            System.out.println("\n=== VICTORY ===");
            System.out.println("You have defeated " + monster.getName() + "!");
        }
    }

    public static void battle(Player player, Monster monster, Scanner scanner){

        System.out.println("Battle start! " + monster.getName() + " appears!");

        while (player.getHealth() > 0 && monster.getHealth() > 0) {
            System.out.println("\nYour HP: " + player.getHealth() + " | " + monster.getName() + " HP: " + monster.getHealth());
            System.out.print("Choose: ATTACK, HEAL, INVENTORY: ");
            String cmd = scanner.nextLine().trim().toUpperCase();

            boolean actionCompleted = false;

            switch (cmd) {
                case "ATTACK":
                    monster.setHealth(monster.getHealth() - player.getCurrentAttack());
                    System.out.println("You dealt " + player.getCurrentAttack() + " damage!");
                    actionCompleted = true;
                    break;

                case "HEAL":
                    System.out.print("Which item? ");
                    String healId = scanner.nextLine().trim().toUpperCase();
                    Item healItem = player.findInInventory(healId);
                    if (healItem != null) {
                        player.heal(healItem);
                        actionCompleted = true;
                    } else {
                        System.out.println("Item not found. Your turn is wasted.");
                        actionCompleted = true;
                    }
                    break;

                case "INVENTORY":
                    player.showInventory();
                    System.out.print("Do you want to EQUIP, UNEQUIP, or CANCEL? ");
                    String action = scanner.nextLine().trim().toUpperCase();
                    if (action.equals("EQUIP")) {
                        System.out.print("Which item? ");
                        String itemId = scanner.nextLine().trim().toUpperCase();
                        Item it = player.findInInventory(itemId);
                        if (it != null) {
                            player.equip(it);
                        } else {
                            System.out.println("Item not found.");
                        }
                        actionCompleted = true;
                    } else if (action.equals("UNEQUIP")) {
                        player.unequip();
                        actionCompleted = true;
                    } else if (action.equals("CANCEL") || action.equals("BACK") || action.equals("EXIT")) {
                        System.out.println("You close your inventory.");
                        actionCompleted = true;
                    } else {
                        System.out.println("Invalid choice. Your turn is wasted.");
                        actionCompleted = true;
                    }
                    break;

                default:
                    System.out.println("Invalid action.");
                    actionCompleted = true;
                    break;
            }

            if (actionCompleted && monster.getHealth() > 0) {
                int dmg = monster.attack();
                player.setHealth(player.getHealth() - dmg);
                System.out.println(monster.getName() + " hits you for " + dmg + " damage!");
            } else if (monster.getHealth() <= 0) {
                System.out.println("You defeated " + monster.getName() + "!");
                monster.setDefeated(true);
            }

            if (player.getHealth() <= 0) {
                System.out.println("You died. Game over.");
                return;
            }
        }

    }
}