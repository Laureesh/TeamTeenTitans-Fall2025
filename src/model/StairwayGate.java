package model;

import controller.GameEngine;

public class StairwayGate {

    public static void displayLockedGate(int floor) {
        String floorLabel = "F" + floor;
        String nextFloorLabel = "F" + (floor - 1);

        System.out.println("\n=====================================");
        System.out.println("         ASCII STAIRS (LOCKED)");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("      ||");
        System.out.println("      ||     ███████████████");
        System.out.println("      ||==== █   LOCKED    █");
        System.out.println("      ||     █ ELEVATOR    █");
        System.out.println("      ||     █  ACCESS     █");
        System.out.println("             █ REQUIRED    █");
        System.out.println("             █ KEY " + floorLabel + "      █");
        System.out.println("             ███████████████");
        System.out.println();
        System.out.println("=====================================");
        System.out.println("\nThis gate is locked. You need a key to proceed to " + nextFloorLabel + ".");
        System.out.println("Type 'use key' to attempt to unlock it.");
    }

    public static void displayUnlockedGate(int floor) {
        String nextFloorLabel = "F" + (floor - 1);

        System.out.println("\n=====================================");
        System.out.println("           ASCII STAIRS (OPEN)");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("      ||");
        System.out.println("      ||     ███████████████");
        System.out.println("      ||==== █   OPENED     █");
        System.out.println("      ||     █  DOORWAY     █");
        System.out.println("      ||     █ DESCENDING   █");
        System.out.println("             █   DOWN…      █");
        System.out.println("             ███████████████");
        System.out.println();
        System.out.println("=====================================");
        System.out.println("\nThe gate is now open! You can proceed to " + nextFloorLabel + ".");
    }

    public static boolean tryUnlockGate(int floor, Player player, GameEngine engine) {
        String requiredKeyId = getRequiredKeyForFloor(floor);

        if (requiredKeyId == null) {
            return false;
        }

        Item key = player.findInInventory(requiredKeyId);
        if (key != null && key instanceof KeyItem) {
            FloorManager.unlockGate(floor);

            int destinationRoom = getDestinationRoomForFloor(floor);
            if (destinationRoom != -1) {
                player.setRoom(destinationRoom);
                System.out.println("\nYou unlock the gate and step through...");
                System.out.println("You find yourself in the hallway of the next floor.");
                if (engine != null) {
                    engine.describeCurrentRoom();
                }
            }
            return true;
        }

        return false;
    }

    private static int getDestinationRoomForFloor(int floor) {
        switch (floor) {
            case 5: return 28;
            case 4: return 29;
            case 3: return 30;
            case 2: return 21;
            default: return -1;
        }
    }

    public static boolean tryUnlockGate(int floor, Player player) {
        return tryUnlockGate(floor, player, null);
    }

    private static String getRequiredKeyForFloor(int floor) {
        switch (floor) {
            case 5: return "Item_5";
            case 4: return "Item_7";
            case 3: return "Item_8";
            case 2: return "Item_9";
            default: return null;
        }
    }

    public static boolean checkExitKey(Player player) {
        Item key = player.findInInventory("Item_10");
        return key != null && key instanceof KeyItem;
    }
}
