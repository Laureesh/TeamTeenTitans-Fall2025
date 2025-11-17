package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class DatabaseManager {

    private static Map<Integer, Room> rooms = new HashMap<>();
    private static Map<Integer, MonsterData> monsters = new HashMap<>();
    private static Map<Integer, PuzzleData> puzzles = new HashMap<>();
    private static Map<Integer, List<String>> puzzleRewards = new HashMap<>();
    private static Map<String, ItemData> items = new HashMap<>();
    private static boolean initialized = false;

    private static String getDataPath(String fileName) throws IOException {
        String[] possiblePaths = {
                "FirstCommitGroup/data/" + fileName,
                "FirstCommitGroup/src/data/" + fileName,
                "data/" + fileName,
                "src/data/" + fileName,
                "../FirstCommitGroup/data/" + fileName,
                "../FirstCommitGroup/src/data/" + fileName,
                "../data/" + fileName,
                "../src/data/" + fileName
        };

        for (String path : possiblePaths) {
            java.nio.file.Path filePath = java.nio.file.Paths.get(path).toAbsolutePath().normalize();
            if (java.nio.file.Files.exists(filePath) && java.nio.file.Files.isRegularFile(filePath)) {
                return path;
            }
        }

        java.nio.file.Path currentDir = java.nio.file.Paths.get("").toAbsolutePath();
        throw new IOException("Cannot find data file: " + fileName +
                "\nCurrent working directory: " + currentDir +
                "\nTried paths: " + String.join(", ", possiblePaths));
    }

    // Validates all rooms are reachable from starting room
    public static void printAllReachableRoomsFrom(int startRoom) {
        initialize();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startRoom);
        visited.add(startRoom);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            Room room = rooms.get(current);
            if (room == null) continue;

            int[] neighbors = {room.north, room.south, room.east, room.west};
            for (int neighbor : neighbors) {
                if (neighbor != -1 && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println("\n=== ROOM REACHABILITY DIAGNOSTIC ===");
        System.out.println("Starting from room " + startRoom + ":");
        System.out.println("Total reachable rooms: " + visited.size());
        System.out.println("Reachable room IDs: " + visited.stream().sorted().collect(java.util.stream.Collectors.toList()));

        Set<Integer> expectedRooms = new HashSet<>();
        for (int i = 1; i <= 26; i++) expectedRooms.add(i);
        expectedRooms.add(27); expectedRooms.add(28); expectedRooms.add(29); expectedRooms.add(30);

        Set<Integer> missing = new HashSet<>(expectedRooms);
        missing.removeAll(visited);

        if (missing.isEmpty()) {
            System.out.println("✓ All 30 rooms (26 regular + 4 hallways) are reachable!");
        } else {
            System.out.println("✗ Missing rooms: " + missing);
        }
        System.out.println("=====================================\n");
    }

    private static void initialize() {
        if (initialized) return;

        try {
            loadRooms();
            loadMonsters();
            loadPuzzles();
            loadPuzzleRewards();
            loadItems();
            initialized = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load game data: " + e.getMessage(), e);
        }
    }

    private static void loadRooms() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getDataPath("rooms.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("ROOM:")) {
                int id = Integer.parseInt(line.substring(5).trim());
                String name = reader.readLine().trim();
                String description = reader.readLine().trim();

                int north = -1, south = -1, east = -1, west = -1, monster = -1, puzzle = -1;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.equals("--------")) break;

                    if (line.startsWith("north=")) north = Integer.parseInt(line.substring(6).trim());
                    else if (line.startsWith("south=")) south = Integer.parseInt(line.substring(6).trim());
                    else if (line.startsWith("east=")) east = Integer.parseInt(line.substring(5).trim());
                    else if (line.startsWith("west=")) west = Integer.parseInt(line.substring(5).trim());
                    else if (line.startsWith("monster=")) monster = Integer.parseInt(line.substring(8).trim());
                    else if (line.startsWith("puzzle=")) puzzle = Integer.parseInt(line.substring(7).trim());
                }

                rooms.put(id, new Room(id, name, description, north, south, east, west, monster, puzzle));
            }
        }
        reader.close();
    }

    private static void loadMonsters() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getDataPath("monsters.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("MONSTER:")) {
                int id = Integer.parseInt(line.substring(8).trim());
                String name = reader.readLine().trim();
                int hp = Integer.parseInt(reader.readLine().trim());
                int attackMin = Integer.parseInt(reader.readLine().trim());
                int attackMax = Integer.parseInt(reader.readLine().trim());
                int defense = Integer.parseInt(reader.readLine().trim());

                monsters.put(id, new MonsterData(id, name, hp, attackMin, attackMax, defense));
            }
        }
        reader.close();
    }

    private static void loadPuzzles() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getDataPath("puzzles.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("PUZZLE:")) {
                int id = Integer.parseInt(line.substring(7).trim());
                String question = reader.readLine().trim();
                String answer = reader.readLine().trim();
                String hint = reader.readLine().trim();

                puzzles.put(id, new PuzzleData(id, question, answer, hint));
            }
        }
        reader.close();
    }

    private static void loadPuzzleRewards() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getDataPath("puzzle_rewards.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("REWARD:")) {
                int puzzleId = Integer.parseInt(line.substring(7).trim());
                String itemId = reader.readLine().trim();

                puzzleRewards.computeIfAbsent(puzzleId, k -> new ArrayList<>()).add(itemId);
            }
        }
        reader.close();
    }

    private static void loadItems() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getDataPath("items.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("ITEM:")) {
                String itemId = line.substring(5).trim();
                String category = readNonEmptyLine(reader);
                int value = Integer.parseInt(readNonEmptyLine(reader));
                String name = readNonEmptyLine(reader);
                String description = readNonEmptyLine(reader);

                items.put(itemId, new ItemData(itemId, category, value, name, description));
            }
        }
        reader.close();
    }

    private static String readNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return "";
    }

    public static Room getRoom(int roomId) {
        initialize();
        return rooms.get(roomId);
    }

    public static MonsterData getMonster(int monsterId) {
        initialize();
        return monsters.get(monsterId);
    }

    public static PuzzleData getPuzzle(int puzzleId) {
        initialize();
        return puzzles.get(puzzleId);
    }

    public static List<String> getPuzzleRewards(int puzzleId) {
        initialize();
        return puzzleRewards.getOrDefault(puzzleId, new ArrayList<>());
    }

    public static ItemData getItem(String itemId) {
        initialize();
        return items.get(itemId);
    }

    public static Item createItemFromData(ItemData data) {
        if (data == null) return null;

        if (data.category.equals("Weapon")) {
            return new WeaponItem(data.itemId, data.name, data.description, data.value);
        } else if (data.category.equals("Armor")) {
            return new ArmorItem(data.itemId, data.name, data.description, data.value, "Torso");
        } else if (data.category.equals("Consumable")) {
            return new ConsumableItem(data.itemId, data.name, data.description, data.value, 0, 1);
        } else if (data.category.equals("Key")) {
            return new KeyItem(data.itemId, data.name, data.description, null);
        }

        return null;
    }

    public static class MonsterData {
        public int id;
        public String name;
        public int hp;
        public int attackMin;
        public int attackMax;
        public int defense;

        MonsterData(int id, String name, int hp, int attackMin, int attackMax, int defense) {
            this.id = id;
            this.name = name;
            this.hp = hp;
            this.attackMin = attackMin;
            this.attackMax = attackMax;
            this.defense = defense;
        }
    }

    public static class PuzzleData {
        public int id;
        public String question;
        public String answer;
        public String hint;

        PuzzleData(int id, String question, String answer, String hint) {
            this.id = id;
            this.question = question;
            this.answer = answer;
            this.hint = hint;
        }
    }

    public static class ItemData {
        public String itemId;
        public String category;
        public int value;
        public String name;
        public String description;

        ItemData(String itemId, String category, int value, String name, String description) {
            this.itemId = itemId;
            this.category = category;
            this.value = value;
            this.name = name;
            this.description = description;
        }
    }
}