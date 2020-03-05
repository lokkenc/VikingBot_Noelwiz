package src.main.java;

import ML.Actions.Action;
import ML.Range.Distance;
import ML.Range.Hp;
import ML.States.State;
import ML.States.StateAction;
import bwapi.*;
import bwta.*;
import ML.Learning.LearningManager;
import ML.Range.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CombatAgent {

    IntelligenceAgent intel;
    ArrayList<LearningManager> models;
    HashMap<Unit, StateAction> UnitStateActionPair;

    public CombatAgent(IntelligenceAgent intel) {
        this.intel = intel;
        this.models = new ArrayList<LearningManager>();
        UnitStateActionPair = new HashMap<Unit, StateAction>();
    }

    public void loadModels() {
        for(LearningManager model : models) {
            model.loadQTable();
        }
    }

    public void storeModels() {
        for(LearningManager model : models) {
            model.storeQTable();
        }
    }

    /**
     * Gives all units of type type the command to attack position targetPos
     * @param self Player assigned to the bot
     * @param type UnitType to send to attack
     * @param targetPos Position to attack
     */
    public void attackPosition (Player self, UnitType type, Position targetPos) {
        if (intel.unitExists(type)) {
            for (Unit unit : self.getUnits()) {
                if (unit.getType() == type) {
                    unit.attack(targetPos, false);
                }
            }
        }
    }

    /**
     * Sends units of type type to attack known enemy bases
     * @param self Player assigned to the bot
     * @param type UnitType that should be sent to attack
     */
    public void attackEnemyBase (Player self, UnitType type) {
        HashSet<Position> enemyBuildingMemory = intel.getEnemyBuildingMemory();
        for (Unit myUnit: self.getUnits()) {
            if(myUnit.getType() == type) {
                if(enemyBuildingMemory.iterator().hasNext()) {
                    myUnit.attack(enemyBuildingMemory.iterator().next());
                } else {
                    myUnit.attack(BWTA.getStartLocations().get(BWTA.getStartLocations().size()).getPosition());
                }
            }
        }
    }

    public void addUnitTypeToModel(UnitType type) {
        boolean add = true;
        for(LearningManager lm: models) {
            if(lm.getUnitType() == type) {
                add = false;
            }
        }

        if(add) {
            this.models.add(new LearningManager(type));
        }
    }

    public void controlArmy(Game game, ArrayList<Unit> allUnits) {
        for(Unit unit: allUnits) {
            for(LearningManager lm: models) {
                if(lm.getUnitType() == unit.getType()) {
                    State currentState = generateState(game, unit);
                    currentState.setUnit(unit);
                    Action action = lm.getNextAction(currentState);
                    if(UnitStateActionPair.get(unit) != null) {
                        State oldState = UnitStateActionPair.get(unit).getState();
                        Action oldAction = UnitStateActionPair.get(unit).getAction();
                        UnitStateActionPair.get(unit).setState(currentState);
                        UnitStateActionPair.get(unit).setAction(action);
                        lm.updateState(oldState, oldAction, currentState);
                    } else {
                        StateAction SA = new StateAction(currentState, action);
                        UnitStateActionPair.put(unit, SA);
                    }
                    action.doAction(game, unit);
                }
            }
        }
    }

    public State generateState(Game game, Unit unit) {
        boolean cooldown = false;
        Distance closestEnemy = null;
        Units numberOfEnemies = null;
        Hp enemyHp = null;
        Hp friendlyHp = null;

        if (unit.isFlying() && unit.getAirWeaponCooldown() > 0) {
            cooldown = true;
        } else if (!unit.isFlying() && unit.getGroundWeaponCooldown() > 0) {
            cooldown = true;
        }

        Unit closestUnit = null;
        int closest = Integer.MAX_VALUE;
        int enemiesInRange = 0;
        int totalEnemyInRangeHP = 0;
        for(Unit units: game.enemy().getUnits()) {
            if(unit.getDistance(units) < closest) {
                closestUnit = units;
                closest = unit.getDistance(units);
            }
            if(unit.isInWeaponRange(units)) {
                enemiesInRange++;
                totalEnemyInRangeHP += units.getHitPoints();
            }
        }

        closestEnemy = new Distance(closest);
        friendlyHp = new Hp(unit.getHitPoints());
        enemyHp = new Hp(totalEnemyInRangeHP);
        numberOfEnemies = new Units(enemiesInRange);

        return new State(cooldown, closestEnemy, numberOfEnemies, enemyHp, friendlyHp);

    }



    public void attackClosestEnemy (Unit unit) {

    }
}
