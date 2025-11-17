package model;

import java.util.*;

/**
 * FloorManager - Manages floor system (F5→F4→F3→F2→F1)
 * Preserves all existing room logic while adding floor awareness
 */
public class FloorManager {

    private static Map<Integer, Integer> roomToFloor = new HashMap<>();
    private static Map<Integer, Integer> floorGates = new HashMap<>(); // floor -> gate room ID
    private static Set<Integer> unlockedGates = new HashSet<>();

    static {
        // Initialize floor mappings per SRS
        // Floor 5: Rooms 1-5 + H1 (room 27)
        for (int i = 1; i <= 5; i++) roomToFloor.put(i, 5);
        roomToFloor.put(27, 5); // H1

        // Floor 4: Rooms 6-10 + H2 (room 28)
        for (int i = 6; i <= 10; i++) roomToFloor.put(i, 4);
        roomToFloor.put(28, 4); // H2

        // Floor 3: Rooms 11-15 + H3 (room 29)
        for (int i = 11; i <= 15; i++) roomToFloor.put(i, 3);
        roomToFloor.put(29, 3); // H3

        // Floor 2: Rooms 16-20 + H4 (room 30)
        for (int i = 16; i <= 20; i++) roomToFloor.put(i, 2);
        roomToFloor.put(30, 2); // H4

        // Floor 1: Rooms 21-25 + H5 (room 26 acts as exit, not hallway)
        for (int i = 21; i <= 25; i++) roomToFloor.put(i, 1);
        roomToFloor.put(26, 1); // Exit room

        // Gate rooms are the hallways themselves (H1-H4)
        // Gates are between floors, so H1 (27) is the gate from F5→F4
        // H2 (28) is the gate from F4→F3, etc.
        floorGates.put(5, 27); // H1: Gate F5→F4
        floorGates.put(4, 28); // H2: Gate F4→F3
        floorGates.put(3, 29); // H3: Gate F3→F2
        floorGates.put(2, 30); // H4: Gate F2→F1
    }

    public static int getFloor(int roomId) {
        return roomToFloor.getOrDefault(roomId, 1);
    }

    public static boolean isGateRoom(int roomId) {
        return floorGates.containsValue(roomId);
    }

    public static int getGateRoomForFloor(int floor) {
        return floorGates.getOrDefault(floor, -1);
    }

    public static boolean isGateUnlocked(int floor) {
        return unlockedGates.contains(floor);
    }

    public static void unlockGate(int floor) {
        unlockedGates.add(floor);
    }

    public static int getFloorForGate(int gateRoomId) {
        for (Map.Entry<Integer, Integer> entry : floorGates.entrySet()) {
            if (entry.getValue() == gateRoomId) {
                return entry.getKey();
            }
        }
        return -1;
    }
}
