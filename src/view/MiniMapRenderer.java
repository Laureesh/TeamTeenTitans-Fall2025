package view;

import model.DatabaseManager;
import model.FloorManager;
import model.Room;

import java.util.*;

/**
 * Color-coded Mini-Map Renderer with ANSI colors
 * RED = floors, CYAN = current room, GREEN = accessible, YELLOW = locked gates
 */
public class MiniMapRenderer {

    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static Map<Integer, Set<Integer>> roomConnections = new HashMap<>();
    private static boolean initialized = false;

    private static void initialize() {
        if (initialized) return;

        for (int i = 1; i <= 30; i++) {
            Room room = DatabaseManager.getRoom(i);
            if (room != null) {
                Set<Integer> connections = new HashSet<>();
                if (room.north != -1) connections.add(room.north);
                if (room.south != -1) connections.add(room.south);
                if (room.east != -1) connections.add(room.east);
                if (room.west != -1) connections.add(room.west);
                roomConnections.put(i, connections);
            }
        }
        initialized = true;
    }

    public static void display(int currentRoomId) {
        initialize();

        int floor = FloorManager.getFloor(currentRoomId);
        System.out.println("\n=== MINI-MAP (FLOOR " + floor + ") ===");

        // Display floor-specific ASCII art
        displayFloorMap(floor, currentRoomId);

        // Show available directions with colors
        Room currentRoom = DatabaseManager.getRoom(currentRoomId);
        if (currentRoom != null) {
            System.out.println("\nAvailable directions:");
            if (currentRoom.north != -1) {
                int targetFloor = FloorManager.getFloor(currentRoom.north);
                String color = (targetFloor == floor) ? ANSI_GREEN : ANSI_YELLOW;
                System.out.println("  ↑ North → " + color + "Room " + currentRoom.north + ANSI_RESET + " (F" + targetFloor + ")");
            }
            if (currentRoom.south != -1) {
                int targetFloor = FloorManager.getFloor(currentRoom.south);
                String color = (targetFloor == floor) ? ANSI_GREEN : ANSI_YELLOW;
                System.out.println("  ↓ South → " + color + "Room " + currentRoom.south + ANSI_RESET + " (F" + targetFloor + ")");
            }
            if (currentRoom.east != -1) {
                int targetFloor = FloorManager.getFloor(currentRoom.east);
                String color = (targetFloor == floor) ? ANSI_GREEN : ANSI_YELLOW;
                System.out.println("  → East → " + color + "Room " + currentRoom.east + ANSI_RESET + " (F" + targetFloor + ")");
            }
            if (currentRoom.west != -1) {
                int targetFloor = FloorManager.getFloor(currentRoom.west);
                String color = (targetFloor == floor) ? ANSI_GREEN : ANSI_YELLOW;
                System.out.println("  ← West → " + color + "Room " + currentRoom.west + ANSI_RESET + " (F" + targetFloor + ")");
            }
        }

        System.out.println();
    }

    private static void displayFloorMap(int floor, int currentRoomId) {
        switch (floor) {
            case 5:
                System.out.println(ANSI_RED + "FLOOR 5" + ANSI_RESET);
                System.out.println("                     " + formatRoom(1, currentRoomId, floor));
                System.out.println("                       |");
                System.out.println(formatRoom(2, currentRoomId, floor) + " -- " +
                        formatRoom(3, currentRoomId, floor) + " -- " +
                        formatRoom(4, currentRoomId, floor) + " -- " +
                        formatRoom(5, currentRoomId, floor));
                System.out.println("                       |");
                System.out.println("                      " + formatRoom(27, currentRoomId, floor) + " (Gate F5→F4)");
                break;
            case 4:
                System.out.println(ANSI_RED + "FLOOR 4" + ANSI_RESET);
                System.out.println(formatRoom(6, currentRoomId, floor) + " -- " +
                        formatRoom(7, currentRoomId, floor) + " -- " +
                        formatRoom(8, currentRoomId, floor) + " -- " +
                        formatRoom(9, currentRoomId, floor) + " -- " +
                        formatRoom(10, currentRoomId, floor));
                System.out.println("                        |");
                System.out.println("                       " + formatRoom(28, currentRoomId, floor) + " (Gate F4→F3)");
                break;
            case 3:
                System.out.println(ANSI_RED + "FLOOR 3" + ANSI_RESET);
                System.out.println(formatRoom(11, currentRoomId, floor) + " -- " +
                        formatRoom(12, currentRoomId, floor) + " -- " +
                        formatRoom(13, currentRoomId, floor) + " -- " +
                        formatRoom(14, currentRoomId, floor) + " -- " +
                        formatRoom(15, currentRoomId, floor));
                System.out.println("                         |");
                System.out.println("                        " + formatRoom(29, currentRoomId, floor) + " (Gate F3→F2)");
                break;
            case 2:
                System.out.println(ANSI_RED + "FLOOR 2" + ANSI_RESET);
                System.out.println(formatRoom(16, currentRoomId, floor) + " -- " +
                        formatRoom(17, currentRoomId, floor) + " -- " +
                        formatRoom(18, currentRoomId, floor) + " -- " +
                        formatRoom(19, currentRoomId, floor) + " -- " +
                        formatRoom(20, currentRoomId, floor));
                System.out.println("                         |");
                System.out.println("                        " + formatRoom(30, currentRoomId, floor) + " (Gate F2→F1)");
                break;
            case 1:
                System.out.println(ANSI_RED + "FLOOR 1" + ANSI_RESET);
                System.out.println(formatRoom(21, currentRoomId, floor) + " -- " +
                        formatRoom(22, currentRoomId, floor) + " -- " +
                        formatRoom(23, currentRoomId, floor) + " -- " +
                        formatRoom(24, currentRoomId, floor) + " -- " +
                        formatRoom(25, currentRoomId, floor));
                System.out.println("                         |");
                System.out.println("                        " + formatRoom(26, currentRoomId, floor) + " (EXIT)");
                break;
        }

        // Show current location indicator
        System.out.println();
        System.out.println(ANSI_CYAN + "               ^" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "         You are here (Room " + currentRoomId + ")" + ANSI_RESET);
    }

    private static String formatRoom(int roomId, int currentRoomId, int currentFloor) {
        int roomFloor = FloorManager.getFloor(roomId);
        boolean isCurrent = (roomId == currentRoomId);
        boolean isGate = FloorManager.isGateRoom(roomId);
        boolean isAccessible = (roomFloor == currentFloor);

        String roomLabel = "R" + roomId;
        String color;

        if (isCurrent) {
            color = ANSI_CYAN;
        } else if (isGate) {
            color = FloorManager.isGateUnlocked(roomFloor) ? ANSI_GREEN : ANSI_YELLOW;
        } else if (isAccessible) {
            color = ANSI_GREEN;
        } else {
            color = ANSI_WHITE;
        }

        return color + "[" + roomLabel + "]" + ANSI_RESET;
    }
}