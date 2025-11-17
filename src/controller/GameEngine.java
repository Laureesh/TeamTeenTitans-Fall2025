package controller;

import model.DatabaseManager;
import model.FloorManager;
import model.Player;
import model.Room;
import view.ConsoleUtil;

import java.util.Scanner;

public class GameEngine {

    public Player player;
    private Scanner scanner = new Scanner(System.in);

    public GameEngine(Player player) {
        this.player = player;
        player.setRoom(1);
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Player getPlayer() {
        return player;
    }

    public void run() {
        System.out.println("\nWelcome to Escaping The Red Cross!");
        System.out.println("Type 'tutorial' if this is your first time, or 'help' for commands.");
        System.out.println();

        describeCurrentRoom();

        while (true) {
            System.out.print("\n> ");
            String cmd = scanner.nextLine();

            EnhancedCommandParser.parse(cmd, this);
        }
    }

    public void describeCurrentRoom() {
        Room room = DatabaseManager.getRoom(player.getRoom());

        if (room == null) {
            System.out.println("\n[Room " + player.getRoom() + "] Unknown Room");
            System.out.println("This room does not exist in the database.");
            return;
        }

        int floor = FloorManager.getFloor(player.getRoom());
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_DIM = "\u001B[90m";
        String ANSI_RESET = "\u001B[0m";

        System.out.println();
        System.out.println(ANSI_CYAN + "[Room " + player.getRoom() + "] " + room.name + " (Floor " + floor + ")" + ANSI_RESET);
        System.out.println(ANSI_DIM + "============================================================" + ANSI_RESET);
        System.out.println();
        System.out.println(ConsoleUtil.wrapText(room.description));

        EnhancedCommandParser.updateLastRoomDescription(this);

        System.out.println("\nAvailable directions:");
        if (room.north != -1) System.out.println("  - north (to room " + room.north + ")");
        if (room.south != -1) System.out.println("  - south (to room " + room.south + ")");
        if (room.east != -1) System.out.println("  - east (to room " + room.east + ")");
        if (room.west != -1) System.out.println("  - west (to room " + room.west + ")");

        if (room.puzzle != -1) {
            System.out.println("\n💡 There is a puzzle here. Type 'use puzzle' to attempt it.");
        }

        if (room.monster != -1) {
            DatabaseManager.MonsterData monster = DatabaseManager.getMonster(room.monster);
            if (monster != null) {
                System.out.println("\n⚠️  A monster is here: " + monster.name + ". Type 'fight' to battle.");
            }
        }
    }
}