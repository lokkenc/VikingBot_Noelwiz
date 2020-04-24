package agents;

import ml.actions.Action;
import ml.data.DataManager;
import ml.model.UnitClassification;
import ml.range.Distance;
import ml.range.Hp;
import ml.state.State;
import ml.state.StateAction;
import bwapi.*;
import bwta.*;
import ml.learning.LearningManager;
import ml.range.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Main Class for Combat control. This Class has some hard coded functions to allow simple combat actions to be taken but
 * in most cases the ControlArmy function should be used.
 */
public class CombatAgent {

    IntelligenceAgent intel;
    ArrayList<LearningManager> models;
    HashMap<Unit, StateAction> UnitStateActionPair;
    private boolean skirmish;

    /**
     * Constructor
     * @param intel intelligence agent
     */
    public CombatAgent(IntelligenceAgent intel) {
        this.intel = intel;
        this.models = new ArrayList<LearningManager>();
        UnitStateActionPair = new HashMap<Unit, StateAction>();
        skirmish = false;
    }

    /**
     * Allow manipulation of the fight/defend switch
     * @param skirmish The new value of the skirmish. True for fight, false for hold position by base
     */
    public void setSkirmish(boolean skirmish) {
        this.skirmish = skirmish;
    }

    /**
     * Retrieves the models for all active unit classifications
     */
    public void loadModels() {
        for(LearningManager model : models) {
            System.out.println("Loading Tables for: " + model.getUnitClassification());
            model.loadQTable();
            model.loadDataManager();
        }
    }

    /**
     * Called on termination of the program to save any changes to all open models
     */
    public void storeModels() {
        for(LearningManager model : models) {
            System.out.println("Storing Tables for: " + model.getUnitClassification());
            model.storeQTable();
            model.storeDataManager();
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

    /**
     * Adds a unit to the list of active models if it does not already exist
     * @param unitClass Units classification into one of the following options: Melee, Ranged
     */
    public void addUnitTypeToModel(UnitClassification unitClass) {
        boolean add = true;
        for(LearningManager lm: models) {
            if(lm.getUnitClassification() == unitClass) {
                add = false;
            }
        }

        if(add) {
            this.models.add(new LearningManager(unitClass));
        }
    }

    /**
     * Loops through all units using their qTable to compute best action based on current state
     * @param game Active game passed from the StrategyAgent
     * @param allUnits List of all units that are currently available
     */
    public void controlArmy(Game game, ArrayList<Unit> allUnits) {
        for(Unit unit: allUnits) {
            for(LearningManager lm: models) {
                if(lm.getUnitClassification() == getUnitClassification(unit.getType())) {
                    State currentState = generateState(game, unit);
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

    /**
     * Get the unitClassification for a specified unitType
     * @param unitType BWAPI information for unitType
     * @return Classification of the given unit
     */
    public UnitClassification getUnitClassification(UnitType unitType) {
        if(unitType.groundWeapon().maxRange() <= 32) { // 32 pixels in game equals 1 range
            return UnitClassification.MELEE;
        } else {
            return UnitClassification.RANGED;
        }
    }

    /**
     * Checks information from given unit inside of the game to determine values for all variables used in a State
     * @param game Active game stored in the StrategyAgent
     * @param unit Unit that needs to create a State
     * @return A State that holds all current information for the given unit
     */
    public State generateState(Game game, Unit unit) {
        // These are the variables that make up a state
        boolean cooldown = false; // weapon cooldown
        Distance closestEnemy = null;
        Units numberOfEnemies = null;
        Units numberOfFriendlies = null;
        Hp enemyHp = null;
        Hp friendlyHp = null;

        if (unit.isFlying() && unit.getAirWeaponCooldown() > 0) {
            cooldown = true;
        } else if (!unit.isFlying() && unit.getGroundWeaponCooldown() > 0) {
            cooldown = true;
        }

        int closest = Integer.MAX_VALUE;
        int enemiesInRange = 0;
        int closestEnemyHp = Integer.MAX_VALUE;
        Unit closestEnemyUnit = null;
        for(Unit units: game.enemy().getUnits()) {
            if(unit.getDistance(units) < closest) {
                closest = unit.getDistance(units);
            }
            if(unit.isInWeaponRange(units)) {
                enemiesInRange++;
                if(units.getHitPoints() < closestEnemyHp) {
                    closestEnemyHp = units.getHitPoints();
                    closestEnemyUnit = units;
                }
            }
        }

        int friendliesInRange = 0;
        for(Unit units: game.self().getUnits()) {
            if(unit.isInWeaponRange(units)) {
                friendliesInRange++;
            }
        }

        closestEnemy = new Distance(closest);
        friendlyHp = new Hp((unit.getHitPoints() / unit.getInitialHitPoints()) * 100);
        if(enemiesInRange > 0) {
            enemyHp = new Hp((closestEnemyUnit.getHitPoints() / closestEnemyUnit.getInitialHitPoints()) * 100);
        } else {
            enemyHp = new Hp(0);
        }
        numberOfEnemies = new Units(enemiesInRange);
        numberOfFriendlies = new Units(friendliesInRange);

        return new State(cooldown, closestEnemy, numberOfEnemies, numberOfFriendlies, enemyHp, friendlyHp, this.skirmish, getUnitClassification(unit.getType()));

    }

    /**
     * Obtain a list of each LearningManagers DataManager
     * @return A List of all DataManagers
     */
    public ArrayList<DataManager> getDataManagers() {
        ArrayList<DataManager> managers = new ArrayList<>(models.size());

        for(LearningManager model : models) {
            managers.add(model.getDataManager());
        }

        return managers;
    }
}
