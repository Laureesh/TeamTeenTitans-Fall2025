package model;

/*
 * Author: Imri Tull
 * Feature: Puzzle Data Model
 */

public class Puzzle {

    public int id;
    public String name;
    public String description;
    public String answer;
    public int attemptsAllowed;
    public String hint;
    public int roomId;

    public Puzzle(int id, String name, String description, String answer,
                  int attemptsAllowed, String hint, int roomId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.answer = answer.toLowerCase();
        this.attemptsAllowed = attemptsAllowed;
        this.hint = hint;
        this.roomId = roomId;
    }
}
