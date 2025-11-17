//************************************************
//Brennon Ary
//ITEC 3860
//11/11/2025
//Main Script
//************************************************

import controller.GameEngine;
import model.Player;

import java.util.Scanner;


public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static String name;

    public static void main(String[] args) {
        // Scanner on System.in should not be closed - it would close standard input
        @SuppressWarnings("resource")
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_YELLOW + "You wake up in a strange environment.");
        System.out.println(ANSI_RESET + "\n");
        System.out.println(ANSI_YELLOW + "Enter your name:");
        name = input.nextLine();
        System.out.println(ANSI_RESET + "You sit up and gather your thoughts. \n" +
                "Your name is " + name + ".\n" +
                "And you're in what looks to be a " + ANSI_YELLOW + "hospital. \n" +
                "It's dead quiet. The hallway outside echos the sound of dripping water. \n" +
                "The hospital gown you dawn provides little air resistance, as you shiver from the cold. \n" +
                "You have to escape... \n" +
                ANSI_RED + "THE RED CROSS");

        Player player = new Player(name);
        System.out.println(ANSI_RESET + "\n");
        GameEngine engine = new GameEngine(player);
        engine.run();

    }



}