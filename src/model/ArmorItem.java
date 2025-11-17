package model;

public class ArmorItem extends Item {
    private int defenseBonus;
    private String slot;  // Head, Torso, Neck

    public ArmorItem(String itemID, String itemName, String description,
                     int defenseBonus, String slot) {
        super(itemID, itemName, description);
        this.defenseBonus = defenseBonus;
        this.slot = slot;
    }

    public int getDefenseBonus() { return defenseBonus; }
    public String getSlot() { return slot; }

    @Override
    public boolean use(Player player) {
        return false;
        //return player.getEquipment().equipArmor(this);
    }
}