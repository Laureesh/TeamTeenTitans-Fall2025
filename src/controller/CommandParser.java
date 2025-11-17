package controller;

import model.*;

public class CommandParser {

    public static void parse(String input, GameEngine engine) {
        input = input.toLowerCase().trim();

        if (input.equals("look")) {
            engine.describeCurrentRoom();
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
            if (direction.equals("north") && room.north != -1) {
                targetRoom = room.north;
            } else if (direction.equals("south") && room.south != -1) {
                targetRoom = room.south;
            } else if (direction.equals("east") && room.east != -1) {
                targetRoom = room.east;
            } else if (direction.equals("west") && room.west != -1) {
                targetRoom = room.west;
            } else {
                System.out.println("You cannot go that way.");
                return;
            }

            engine.player.setRoom(targetRoom);
            System.out.println("\nYou move to room " + targetRoom + "...");
            engine.describeCurrentRoom();
            return;
        }

        if (input.equals("use puzzle") || input.equals("use vending")) {
            Puzzle p = PuzzleManager.loadPuzzle(engine.player.getRoom());

            if (p == null) {
                System.out.println("There is no puzzle in this room.");
                return;
            }

            PuzzleEngine.startPuzzle(p, engine.player);
            return;
        }

        if (input.equals("fight")) {
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

            String monsterID = "M" + monsterData.id;
            String name = monsterData.name;
            int hp = monsterData.hp;
            int attackMin = monsterData.attackMin;
            int attackMax = monsterData.attackMax;
            int defense = monsterData.defense;

            String description = "A hostile creature.";
            String location = "R" + engine.player.getRoom();
            String drops = null;

            Monster m = new Monster(monsterID, name, description, hp, attackMin, attackMax, defense, location, drops);
            Battle.battle(engine.player, m, engine.getScanner());
            return;
        }

        System.out.println("Unknown command. Try: 'look', 'go north/south/east/west', 'use puzzle', or 'fight'");
    }
}