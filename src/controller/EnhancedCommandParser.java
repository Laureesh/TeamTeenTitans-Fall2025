package controller;

import model.*;
import view.ConsoleUtil;
import view.MiniMapRenderer;

import java.util.*;

public class EnhancedCommandParser {

    private static int lastRoomId = -1;
    private static boolean debugMode = false;
    private static Set<Integer> solvedPuzzles = new HashSet<>();

    public static void parse(String input, GameEngine engine) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("The janitor, in his confusion, punches himself in the face.");
            return;
        }

        input = input.toLowerCase().trim();

        if (input.equals("forward") || input.equals("f")) {
            input = "go east";
        } else if (input.equals("back") || input.equals("b") || input.equals("leave")) {
            if (lastRoomId != -1 && lastRoomId != engine.player.getRoom()) {
                engine.player.setRoom(lastRoomId);
                System.out.println("\nYou go back...");
                engine.describeCurrentRoom();
                updateLastRoom(engine);
                return;
            } else {
                System.out.println("You can't go back from here.");
                return;
            }
        }

        if (input.length() == 1) {
            switch (input) {
                case "n": input = "go north"; break;
                case "s": input = "go south"; break;
                case "e": input = "go east"; break;
                case "w": input = "go west"; break;
            }
        }

        if (input.equals("look") || input.equals("lo")) {
            engine.describeCurrentRoom();
            return;
        }

        if (input.equals("inventory") || input.equals("i")) {
            engine.player.showInventory();
            return;
        }

        if (input.equals("grab") || input.equals("g")) {
            System.out.println("There are no items to grab here.");
            return;
        }

        if (input.equals("equip") || input.equals("e")) {
            System.out.println("Usage: equip <item-id>");
            return;
        }
        if (input.startsWith("equip ") || input.startsWith("e ")) {
            String itemId = input.substring(input.indexOf(" ") + 1).trim().toUpperCase();
            Item item = engine.player.findInInventory(itemId);
            if (item != null) {
                engine.player.equip(item);
            } else {
                System.out.println("Item not found in inventory.");
            }
            return;
        }

        if (input.equals("drop") || input.equals("d")) {
            System.out.println("Usage: drop <item-id>");
            return;
        }
        if (input.startsWith("drop ") || input.startsWith("d ")) {
            System.out.println("Dropping items is not implemented yet.");
            return;
        }

        if (input.equals("use") || input.equals("u")) {
            System.out.println("Usage: use <item-id> or use puzzle");
            return;
        }
        if (input.startsWith("use ")) {
            String target = input.substring(4).trim();

            if (target.equals("key") || target.startsWith("key ")) {
                handleKeyUsage(engine);
                return;
            }

            if (target.equals("puzzle") || target.equals("vending")) {
                Room room = DatabaseManager.getRoom(engine.player.getRoom());
                if (room == null || room.puzzle == -1) {
                    System.out.println("There is no puzzle in this room.");
                    return;
                }

                if (solvedPuzzles.contains(room.puzzle)) {
                    System.out.println("You have already solved this puzzle.");
                    return;
                }

                Puzzle p = PuzzleManager.loadPuzzle(engine.player.getRoom());
                if (p == null) {
                    System.out.println("There is no puzzle in this room.");
                    return;
                }

                PuzzleManager.startPuzzle(p, engine.player, solvedPuzzles);
                return;
            }

            Item item = engine.player.findInInventory(target.toUpperCase());
            if (item != null) {
                if (item instanceof ConsumableItem) {
                    engine.player.heal(item);
                } else if (item instanceof WeaponItem || item instanceof ArmorItem) {
                    engine.player.equip(item);
                } else {
                    System.out.println("You can't use that item here.");
                }
            } else {
                System.out.println("Item not found.");
            }
            return;
        }

        if (input.equals("attack") || input.equals("atk") || input.equals("a") || input.equals("hit") || input.equals("fight")) {
            Room room = DatabaseManager.getRoom(engine.player.getRoom());

            if (room == null || room.monster == -1) {
                System.out.println("There is nothing to fight here.");
                return;
            }

            DatabaseManager.MonsterData monsterData = DatabaseManager.getMonster(room.monster);

            if (monsterData == null) {
                System.out.println("There is nothing to fight here.");
                return;
            }

            Battle.startBattle(engine.player, monsterData, engine.getScanner());
            return;
        }

        if (input.equals("flee") || input.equals("fl")) {
            System.out.println("You can't flee right now.");
            return;
        }

        if (input.equals("help")) {
            displayHelp();
            return;
        }

        if (input.equals("tutorial")) {
            displayTutorial();
            return;
        }

        if (input.equals("clear")) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
            return;
        }

        if (input.equals("heal") || input.equals("stats") || input.equals("health")) {
            System.out.println("The janitor, in his confusion, punches himself in the face.");
            return;
        }

        if (input.equals("map")) {
            MiniMapRenderer.display(engine.player.getRoom());
            return;
        }

        if (debugMode) {
            if (input.startsWith("debug teleport ")) {
                try {
                    int room = Integer.parseInt(input.substring(15).trim());
                    engine.player.setRoom(room);
                    System.out.println("Teleported to room " + room);
                    engine.describeCurrentRoom();
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid room number.");
                    return;
                }
            }
            if (input.startsWith("debug sethp ")) {
                try {
                    int hp = Integer.parseInt(input.substring(12).trim());
                    engine.player.setHealth(hp);
                    System.out.println("HP set to " + hp);
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid HP value.");
                    return;
                }
            }
            if (input.equals("debug toggle")) {
                debugMode = false;
                System.out.println("Debug mode OFF");
                return;
            }
        }

        if (input.equals("debug")) {
            debugMode = true;
            System.out.println("Debug mode ON. Commands: 'debug teleport <room>', 'debug sethp <value>', 'debug toggle'");
            return;
        }

        if (input.startsWith("go ")) {
            String direction = input.substring(3).trim();
            Room room = DatabaseManager.getRoom(engine.player.getRoom());

            if (room == null) {
                System.out.println("You are in an unknown location.");
                return;
            }

            int targetRoom = -1;
            String dirName = "";
            if (direction.equals("north") || direction.equals("n")) {
                targetRoom = room.north;
                dirName = "north";
            } else if (direction.equals("south") || direction.equals("s")) {
                targetRoom = room.south;
                dirName = "south";
            } else if (direction.equals("east") || direction.equals("e")) {
                targetRoom = room.east;
                dirName = "east";
            } else if (direction.equals("west") || direction.equals("w")) {
                targetRoom = room.west;
                dirName = "west";
            } else {
                System.out.println("The janitor, in his confusion, punches himself in the face.");
                return;
            }

            if (targetRoom == -1) {
                System.out.println("You can't go " + dirName + " from here.");
                System.out.println("Available directions: " + getAvailableDirections(room));
                return;
            }

            int currentRoom = engine.player.getRoom();
            if (FloorManager.isGateRoom(currentRoom) && direction.equals("south")) {
                int floor = FloorManager.getFloorForGate(currentRoom);
                if (floor != -1 && !FloorManager.isGateUnlocked(floor)) {
                    System.out.println("\nThe gate is locked! You need a key to proceed.");
                    StairwayGate.displayLockedGate(floor);
                    return;
                }
            }

            if (FloorManager.isGateRoom(targetRoom) && direction.equals("north")) {
                int floor = FloorManager.getFloorForGate(targetRoom);
                if (floor != -1 && !FloorManager.isGateUnlocked(floor)) {
                    System.out.println("\nYou approach the gate...");
                    StairwayGate.displayLockedGate(floor);
                    return;
                }
            }

            if (targetRoom == 26) {
                if (!StairwayGate.checkExitKey(engine.player)) {
                    System.out.println("\nThe exit is locked! You need the Entrance Key to proceed.");
                    System.out.println("This key doesn't unlock this door.");
                    return;
                }
            }

            updateLastRoom(engine);

            engine.player.setRoom(targetRoom);
            System.out.println("\nYou move to room " + targetRoom + "...");

            if (FloorManager.isGateRoom(targetRoom)) {
                int floor = FloorManager.getFloorForGate(targetRoom);
                if (floor != -1) {
                    if (FloorManager.isGateUnlocked(floor)) {
                        StairwayGate.displayUnlockedGate(floor);
                    } else {
                        StairwayGate.displayLockedGate(floor);
                    }
                }
            }

            engine.describeCurrentRoom();
            updateLastRoomDescription(engine);
            return;
        }

        if (input.equals("exit") || input.equals("quit")) {
            System.out.println("Thanks for playing! Goodbye.");
            System.exit(0);
            return;
        }

        System.out.println("The janitor, in his confusion, punches himself in the face.");
    }

    private static void handleKeyUsage(GameEngine engine) {
        int currentRoom = engine.player.getRoom();

        if (!FloorManager.isGateRoom(currentRoom)) {
            System.out.println("There is no gate here to unlock.");
            return;
        }

        int floor = FloorManager.getFloorForGate(currentRoom);
        if (floor == -1) {
            System.out.println("Unable to determine which gate this is.");
            return;
        }

        if (FloorManager.isGateUnlocked(floor)) {
            StairwayGate.displayUnlockedGate(floor);
            return;
        }

        if (StairwayGate.tryUnlockGate(floor, engine.player, engine)) {
        } else {
            System.out.println("This key doesn't unlock this door.");
            StairwayGate.displayLockedGate(floor);
        }
    }

    private static String getAvailableDirections(Room room) {
        List<String> dirs = new ArrayList<>();
        if (room.north != -1) dirs.add("north");
        if (room.south != -1) dirs.add("south");
        if (room.east != -1) dirs.add("east");
        if (room.west != -1) dirs.add("west");
        return dirs.isEmpty() ? "none" : String.join(", ", dirs);
    }

    private static void updateLastRoom(GameEngine engine) {
        lastRoomId = engine.player.getRoom();
    }

    public static void updateLastRoomDescription(GameEngine engine) {
    }

    private static void displayHelp() {
        System.out.println("\n==================== HELP MENU ====================");
        System.out.println();
        System.out.println("Movement:");
        System.out.println("  forward | f | go north/south/east/west | n/s/e/w");
        System.out.println("  back | b | leave");
        System.out.println();
        System.out.println("Interaction:");
        System.out.println("  look | lo");
        System.out.println("  grab | g");
        System.out.println("  equip | e");
        System.out.println("  drop | d");
        System.out.println("  use | u");
        System.out.println();
        System.out.println("Combat:");
        System.out.println("  attack | atk | a | hit");
        System.out.println("  flee | fl");
        System.out.println();
        System.out.println("Player:");
        System.out.println("  inventory | i");
        System.out.println();
        System.out.println("Utility:");
        System.out.println("  help");
        System.out.println("===================================================\n");
    }

    private static void displayTutorial() {
        System.out.println("\n" + ConsoleUtil.wrapText(
                "=== TUTORIAL: ESCAPING THE RED CROSS ===\n" +
                        "Welcome! This tutorial will teach you the basics of the game.\n"
        ));

        System.out.println("\n--- MOVEMENT ---");
        System.out.println(ConsoleUtil.wrapText(
                "Use 'go north', 'go south', 'go east', or 'go west' to move between rooms. " +
                        "You can also use shortcuts: 'n', 's', 'e', 'w'. " +
                        "Type 'look' to see your current room again, or 'map' to view the mini-map."
        ));

        System.out.println("\n--- PUZZLES ---");
        System.out.println(ConsoleUtil.wrapText(
                "Some rooms contain vending machines with puzzles. Type 'use puzzle' to interact with them. " +
                        "Solve the riddle to receive rewards! You have limited attempts, so think carefully."
        ));

        System.out.println("\n--- COMBAT ---");
        System.out.println(ConsoleUtil.wrapText(
                "When you encounter a monster, type 'fight' to start combat. " +
                        "During battle, you can ATTACK, HEAL (use items), or check your INVENTORY. " +
                        "Defeat monsters to progress through the facility."
        ));

        System.out.println("\n--- ITEMS ---");
        System.out.println(ConsoleUtil.wrapText(
                "Items can be found as rewards from puzzles or dropped by monsters. " +
                        "Type 'inventory' to see what you're carrying. " +
                        "Use items during combat or when needed. Weapons and armor can be equipped to boost your stats."
        ));

        System.out.println("\n--- MAP ---");
        System.out.println(ConsoleUtil.wrapText(
                "Type 'map' at any time to see a visual representation of nearby rooms. " +
                        "The map shows your current location and available paths."
        ));

        System.out.println("\n--- BASIC COMMANDS ---");
        System.out.println(ConsoleUtil.wrapText(
                "• 'help' or 'commands' - Show this help menu\n" +
                        "• 'stats' - View your character statistics\n" +
                        "• 'inventory' - View your items\n" +
                        "• 'whereami' - Show current room information\n" +
                        "• 'clear' - Clear the screen (prints blank lines)\n" +
                        "• 'tutorial' - Show this tutorial again"
        ));

        System.out.println("\n" + ConsoleUtil.wrapText(
                "That's the basics! Explore the facility, solve puzzles, defeat monsters, and escape THE RED CROSS. " +
                        "Type 'help' anytime for a quick reference.\n"
        ));

        ConsoleUtil.printSeparator();
    }
}