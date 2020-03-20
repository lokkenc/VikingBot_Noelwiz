package Planning;

import bwapi.*;

public class CombatUnitStatus {
    private Unit combatUnit;
    private int amount;

    public void setCombatUnit(Unit combatUnit) {
        this.combatUnit = combatUnit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Unit getCombatUnit() {
        return combatUnit;
    }

    public int getAmount() {
        return amount;
    }
}
