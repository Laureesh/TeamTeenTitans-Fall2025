package model;

public class ConsumableItem extends Item {
    private int healingAmount;
    private int attackBoost;
    private int uses;

    public ConsumableItem(String itemID, String itemName, String description,
                          int healingAmount, int attackBoost, int uses) {
        super(itemID, itemName, description);
        this.healingAmount = healingAmount;
        this.attackBoost = attackBoost;
        this.uses = uses;
    }

    public int getUses() { return uses; }

    @Override
    public boolean use(Player player) {
        if (uses <= 0) return false;

        if (healingAmount > 0)
            player.heal(healingAmount);

        if (attackBoost > 0)
            player.applyTempAttackBoost(attackBoost);

        uses--;
        return true;
    }
}