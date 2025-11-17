package model;

public abstract class Item {
    protected String itemID;
    protected String itemName;
    protected String description;

    public Item(String itemID, String itemName, String description) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.description = description;
    }

    public String getItemID() { return itemID; }
    public String getItemName() { return itemName; }
    public String getDescription() { return description; }

    // Returns true if successfully used, false if not
    public abstract boolean use(Player player);

    public String getName() {
        return itemName;
    }
}