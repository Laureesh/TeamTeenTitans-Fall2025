package model;

import java.util.HashMap;
import java.util.Map;

public class Equipment {

    private WeaponItem equippedWeapon;

    // Armor slots (Head, Torso, Neck)
    private Map<String, ArmorItem> equippedArmor = new HashMap<>();

    public boolean equipWeapon(WeaponItem weapon) {
        this.equippedWeapon = weapon;
        return true;
    }

    public WeaponItem getEquippedWeapon() {
        return equippedWeapon;
    }

    public int getWeaponAttackBonus() {
        return equippedWeapon != null ? equippedWeapon.getAttackBonus() : 0;
    }

    public boolean equipArmor(ArmorItem armor) {
        equippedArmor.put(armor.getSlot(), armor);
        return true;
    }

    public int getTotalDefenseBonus() {
        int total = 0;
        for (ArmorItem armor : equippedArmor.values()) {
            total += armor.getDefenseBonus();
        }
        return total;
    }
}