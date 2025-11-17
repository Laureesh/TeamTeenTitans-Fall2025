package model;

import java.util.ArrayList;
import java.util.List;

//************************************************
//Brennon Ary
//ITEC 3860
//11/11/2025
//Player Script
//************************************************
public class Player {

    private int currentRoom;
    private String name;
    private List<Item> inventory;
    private Item equippedItem;
    private int health;
    private int baseAttack;
    private int currentAttack;

    private Equipment equipment; // <-- ADD THIS

    public Player(String name) {
        this.name = name;
        this.currentRoom = 0;
        this.health = 100;
        this.baseAttack = 10;
        this.currentAttack = baseAttack;
        this.equipment = new Equipment();  // <-- INITIALIZE EQUIPMENT
        this.inventory = new ArrayList<>();
    }

    public Equipment getEquipment() {
        return equipment;
    }
    public int getAttack() {
        return baseAttack + equipment.getWeaponAttackBonus();
    }
    public void applyTempAttackBoost(int attackBoost) {
        currentAttack += attackBoost;
    }
    public void heal(int healingAmount) {
        health += healingAmount;
    }

    public Item findInInventory(String id) {
        if (inventory == null || inventory.isEmpty()) {
            return null;
        }
        for (Item item : inventory) {
            if (item.getItemID().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    public void showInventory() {
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("=== INVENTORY ===");
        for (Item item : inventory) {
            System.out.println("- [" + item.getItemName() + "]: " + item.getDescription());
        }
    }

    public void equip(Item item) {
        if (item == null) {
            System.out.println("Item not found in inventory.");
            return;
        }
        if (item instanceof WeaponItem) {
            equipment.equipWeapon((WeaponItem) item);
            equippedItem = item;
            System.out.println("Equipped weapon: " + item.getName());
        } else if (item instanceof ArmorItem) {
            equipment.equipArmor((ArmorItem) item);
            equippedItem = item;
            System.out.println("Equipped armor: " + item.getName());
        } else {
            System.out.println("Cannot equip this item type.");
        }
    }

    public void unequip() {
        if (equippedItem == null) {
            System.out.println("No item is currently equipped.");
            return;
        }
        System.out.println("Unequipped: " + equippedItem.getName());
        equippedItem = null;
    }

    public void heal(Item item) {
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            if (consumable.use(this)) {
                System.out.println("Used " + item.getName());
            } else {
                System.out.println("Item has no uses left.");
            }
        } else {
            System.out.println("This item cannot be used for healing.");
        }
    }

    // === Player Stats ===
    public void stats() {
        System.out.println("===PLAYER STATS===");
        System.out.println("Name: " + name);
        System.out.println("HP: " + health);
        System.out.println("Attack: " + currentAttack);
        if (equippedItem != null) {
            System.out.println("Equipped: " + equippedItem.getName());
        } else {
            System.out.println("Equipped: None");
        }
    }

    // === Getters / Setters ===
    public List<Item> getInventory() { return inventory; }
    public String getName() { return name; }
    public int getRoom() { return currentRoom; }
    public void setRoom(int newRoom) { this.currentRoom = newRoom; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(0, health); }

    public Item getEquippedItem() { return equippedItem; }

    public int getCurrentAttack() { return currentAttack; }
}