package planning;

import bwapi.*;

public class CombatUnitStatus {
    private UnitType combatUnit;
    private int amount;

    public CombatUnitStatus(UnitType unit, int amountOwned){
        this.combatUnit = unit;
        this.amount = amountOwned;
    }

    public void setCombatUnit(UnitType combatUnit) {
        this.combatUnit = combatUnit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UnitType getCombatUnit() {
        return combatUnit;
    }

    public int getAmount() {
        return amount;
    }
}
