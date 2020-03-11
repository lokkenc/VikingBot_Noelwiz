package Planning.Actions;

import Planning.CombatUnitStatus;
import bwapi.UnitType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AttackActionTypeTest {

    @Test
    void typeName() {
        assertEquals("Attack", new AttackActionType().typeName(), "attack action should " +
                "be called Attack");
    }

    @Test
    void associatedAction() {
        AttackAction atk = new AttackAction("");
        assertEquals(atk.actionName(), new AttackActionType().associatedAction(""), "attack action should create consistant actions");
    }

    @Test
    void allApplicableActions() {
        //TODO: test some day.
    }

    @Test
    void haveCombatUnits() {
        ArrayList<CombatUnitStatus> normalBotUnits = new ArrayList<>(2);
        normalBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Zealot, 10));
        normalBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Probe,5));
        assertTrue(AttackActionType.HaveCombatUnits(normalBotUnits), "Should be able to tell" +
                "when we have combat units");

        ArrayList<CombatUnitStatus> empty = new ArrayList<>(2);
        assertFalse(AttackActionType.HaveCombatUnits(empty), "Empty list has no combat units");

        ArrayList<CombatUnitStatus> notFirstUnitBotUnits = new ArrayList<>(2);
        //FUTURE IMPROVEMENT: ACCOUNT FOR STUFF LIKE CARRIERS WHICH INDIRECTLY ATTACK
        //ignoring for now due to effort
        //notFirstUnitBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Carrier, 10));
        notFirstUnitBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Observer, 10));
        notFirstUnitBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Probe, 10));
        notFirstUnitBotUnits.add(new CombatUnitStatus(UnitType.Protoss_Zealot,5));
        assertTrue(AttackActionType.HaveCombatUnits(notFirstUnitBotUnits), "Should be able to tell" +
                "when we have combat units, even if they're not first in the list");

        ArrayList<CombatUnitStatus> negativeUnits = new ArrayList<>(5);
        negativeUnits.add(new CombatUnitStatus(UnitType.Protoss_Zealot, -1));
        negativeUnits.add(new CombatUnitStatus(UnitType.Protoss_Probe, 10));
        assertFalse(AttackActionType.HaveCombatUnits(negativeUnits), "Test case for tests. should never be encountered irl");


        ArrayList<CombatUnitStatus> CombatAllDead = new ArrayList<>(2);
        CombatAllDead.add(new CombatUnitStatus(UnitType.Protoss_Zealot, 0));
        CombatAllDead.add(new CombatUnitStatus(UnitType.Protoss_Probe, 10));
        CombatAllDead.add(new CombatUnitStatus(UnitType.Protoss_Observer,5));
        assertFalse(AttackActionType.HaveCombatUnits(CombatAllDead), "Dead Combat units means no combat units");

        ArrayList<CombatUnitStatus> onlyOneCombatUnit = new ArrayList<>(2);
        onlyOneCombatUnit.add(new CombatUnitStatus(UnitType.Protoss_Zealot, 1));
        onlyOneCombatUnit.add(new CombatUnitStatus(UnitType.Protoss_Carrier,5));
        assertTrue(AttackActionType.HaveCombatUnits(onlyOneCombatUnit), "Should be able to tell" +
                "when we have combat units, even if it's only one");


    }
}