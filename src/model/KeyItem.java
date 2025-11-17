package model;

public class KeyItem extends Item {
    private String unlocks; // F1, F2, Entrance, etc.

    public KeyItem(String itemID, String itemName, String description, String unlocks) {
        super(itemID, itemName, description);
        this.unlocks = unlocks;
    }

    public String getUnlocks() {
        return unlocks;
    }

    @Override
    public boolean use(Player player) {
        // GameController or Map handles unlock logic
        //return true;
        return false;
    }
}