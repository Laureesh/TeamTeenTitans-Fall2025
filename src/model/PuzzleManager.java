package model;

import view.ConsoleUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PuzzleManager {

    private static final Scanner scanner = new Scanner(System.in);

    public static Puzzle loadPuzzle(int roomId) {
        Room room = DatabaseManager.getRoom(roomId);
        if (room == null || room.puzzle == -1) return null;

        DatabaseManager.PuzzleData data = DatabaseManager.getPuzzle(room.puzzle);
        if (data == null) return null;

        return new Puzzle(
                data.id,
                "Puzzle " + data.id,
                data.question,
                data.answer,
                3,
                data.hint,
                roomId
        );
    }

    public static void giveRewards(int puzzleId, Player player) {
        List<String> rewardItemIds = DatabaseManager.getPuzzleRewards(puzzleId);

        for (String itemId : rewardItemIds) {
            DatabaseManager.ItemData itemData = DatabaseManager.getItem(itemId);
            if (itemData != null) {
                Item item = DatabaseManager.createItemFromData(itemData);
                if (item != null) {
                    player.getInventory().add(item);
                    System.out.println("  + Received: " + item.getItemName());
                }
            }
        }
    }

    public static void startPuzzle(Puzzle puzzle, Player player, Set<Integer> solvedPuzzles) {
        System.out.println("\n=== PUZZLE ENCOUNTER ===");
        System.out.println("A vending machine blocks your path. Its screen displays:");
        System.out.println();
        System.out.println("🧩 " + puzzle.name);
        System.out.println(ConsoleUtil.wrapText(puzzle.description));
        System.out.println();
        System.out.println("Attempts allowed: " + puzzle.attemptsAllowed);
        System.out.println("Type your answer below, or 'back' to exit.");
        System.out.println();

        int attempts = 0;

        while (attempts < puzzle.attemptsAllowed) {
            System.out.print("Answer (attempt " + (attempts + 1) + "/" + puzzle.attemptsAllowed + "): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("back")) {
                System.out.println("You step away from the vending machine.");
                return;
            }

            if (input.equals(puzzle.answer.toLowerCase())) {
                System.out.println("\n✔ CORRECT! The vending machine whirs to life...");
                giveRewards(puzzle.id, player);
                System.out.println("Rewards have been added to your inventory.");
                solvedPuzzles.add(puzzle.id);
                System.out.println("\n=== PUZZLE SOLVED ===");
                return;
            }

            attempts++;
            System.out.println("❌ Incorrect. Attempts used: " + attempts + "/" + puzzle.attemptsAllowed);

            if (attempts == puzzle.attemptsAllowed) {
                System.out.println("\nThe vending machine shuts down. You have run out of attempts.");
                System.out.println("You step away from the puzzle.");
                return;
            }

            if (attempts == puzzle.attemptsAllowed - 1) {
                System.out.println("\n💡 Hint: " + ConsoleUtil.wrapText(puzzle.hint));
            }
        }
    }
}