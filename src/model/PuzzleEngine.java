package model;

import view.ConsoleUtil;

import java.util.Scanner;

public class PuzzleEngine {

    private static final Scanner scanner = new Scanner(System.in);

    public static void startPuzzle(Puzzle puzzle, Player player) {
        int attempts = 0;

        System.out.println("\n🧩 Puzzle: " + puzzle.name);
        System.out.println(ConsoleUtil.wrapText(puzzle.description));

        while (attempts < puzzle.attemptsAllowed) {

            System.out.print("\nEnter answer (or 'back' to exit): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("back")) {
                System.out.println("You exit the puzzle.");
                return;
            }

            if (input.equals(puzzle.answer.toLowerCase())) {
                System.out.println("\n✔ Correct! You solved the puzzle.");
                PuzzleManager.giveRewards(puzzle.id, player);
                System.out.println("Rewards have been added to your inventory.");
                return;
            }

            attempts++;
            System.out.println("❌ Incorrect. Attempts used: " + attempts);

            if (attempts == puzzle.attemptsAllowed) {
                System.out.println("\nThe vending machine does nothing...");
                System.out.println("You have exited the puzzle.");
                return;
            }

            if (attempts == puzzle.attemptsAllowed - 1) {
                System.out.println("\n💡 Hint: " + ConsoleUtil.wrapText(puzzle.hint));
            }
        }
    }
}