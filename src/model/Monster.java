package model;

public class Monster {
    private String monsterID;
    private String name;
    private String description;
    private int health;
    private int maxHealth;
    private int attackMin;
    private int attackMax;
    private int defense;
    private String location;    // "R4", "R13", etc.
    private String drops;       // Item ID dropped
    private boolean defeated;

    public Monster(String monsterID, String name, String description,
                   int health, int attackMin, int attackMax,
                   int defense, String location, String drops) {

        this.monsterID = monsterID;
        this.name = name;
        this.description = description;
        this.health = health;
        this.maxHealth = health;
        this.attackMin = attackMin;
        this.attackMax = attackMax;
        this.defense = defense;
        this.location = location;
        this.drops = drops;
    }

    // ---------------------------
    // Combat Logic
    // ---------------------------

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int playerDamage) {
        int dmg = Math.max(0, playerDamage - defense);
        health -= dmg;
    }

    public int attack() {
        return attackMin + (int)(Math.random() * ((attackMax - attackMin) + 1));
    }

    // ---------------------------
    // Getters
    // ---------------------------

    public String getMonsterID() { return monsterID; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDefense() { return defense; }
    public int getAttackMin() { return attackMin; }
    public int getAttackMax() { return attackMax; }

    public String getLocation() { return location; }
    public String getDrops() { return drops; }
    public boolean isDefeated() { return defeated; }

    // ---------------------------
    // Setters
    // ---------------------------

    public void setHealth(int health) { this.health = health; }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }
}

