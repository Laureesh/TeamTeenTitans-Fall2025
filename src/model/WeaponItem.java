package model;

public class WeaponItem extends Item {
    private int attackBonus;

    public WeaponItem(String itemID, String itemName, String description, int attackBonus) {
        super(itemID, itemName, description);
        this.attackBonus = attackBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public boolean use(Player player) {
        return player.getEquipment().equipWeapon(this);
    }
}