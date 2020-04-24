package planning;

import burlap.mdp.core.state.State;
import burlap.statehashing.HashableState;
import bwapi.*;

import java.util.ArrayList;
import java.util.List;

public class PlanningState implements HashableState, State {
    private int numWorkers;
    private int mineralProductionRate;
    private int gasProductionRate;
    private int numBases;
    private int timeSinceLastScout;
    private ArrayList<CombatUnitStatus> combatUnitStatuses;
    private int numEnemyWorkers;
    private int numEnemyBases;
    private UnitType mostCommonCombatUnit;
    private Boolean attackingEnemyBase;
    private Boolean beingAttacked;
    private Race playerRace;
    private Race enemyRace;
    private GameStatus gameStatus;
    private int[][] trainingCapacity; /*[ workers[used, avail] , ground[used, avail], combat air[used, avail],
                                            support air[used, avail]] */

    private ArrayList<String> VariableKeys = new ArrayList<String>();


    public PlanningState(int numWorkers, int mineralProductionRate, int gasProductionRate, int numBases,
                         int timeSinceLastScout, ArrayList<CombatUnitStatus> combatUnitStatuses, int numEnemyWorkers,
                         int numEnemyBases, UnitType mostCommonCombatUnit, Boolean attackingEnemyBase, Boolean beingAttacked,
                         Race playerRace, Race enemyRace, GameStatus gameStatus, int[][] trainingCapacity) {
        this.numWorkers = numWorkers;
        this.mineralProductionRate = mineralProductionRate;
        this.gasProductionRate = gasProductionRate;
        this.numBases = numBases;
        this.timeSinceLastScout = timeSinceLastScout;
        this.combatUnitStatuses = combatUnitStatuses;
        this.numEnemyWorkers = numEnemyWorkers;
        this.numEnemyBases = numEnemyBases;
        this.mostCommonCombatUnit = mostCommonCombatUnit;
        this.attackingEnemyBase = attackingEnemyBase;
        this.beingAttacked = beingAttacked;
        this.playerRace = playerRace;
        this.enemyRace = enemyRace;
        this.gameStatus = gameStatus;
        this.trainingCapacity = trainingCapacity;

        VariableKeys.add("numWorkers");
        VariableKeys.add("mineralProductionRate");
        VariableKeys.add("gasProductionRate");
        VariableKeys.add("numBases");
        VariableKeys.add("timeSinceLastScout");
        VariableKeys.add("combatUnitStatuses");
        VariableKeys.add("numEnemyWorkers");
        VariableKeys.add("numEnemyBases");
        VariableKeys.add("mostCommonCombatUnit");
        VariableKeys.add("attackingEnemyBase");
        VariableKeys.add("beingAttacked");
        VariableKeys.add("playerRace");
        VariableKeys.add("enemyRace");
        VariableKeys.add("gameStatus");
        VariableKeys.add("trainingCapacity");
    }

    /**
     *
     * @param variableKey a string that's the key to one of the variables. Present
     *                    in the VariableKeys list.
     * @return a Object that is contained in that variable
     */


    public Object get(Object variableKey) {
        /*public class test{
             public static void main(String[] args) {
                Object testobj = new String("words");
                System.out.println(testobj.toString());
             }
        }
        outputs: "words" so the lines below should be fine*/
        String key = variableKey.toString();
        Object result;



        //String implementation
        switch(key){
            case "numWorkers":
                result = numWorkers;
                break;
            case "mineralProductionRate":
                result = mineralProductionRate;
                break;
            case "gasProductionRate":
                result = gasProductionRate;
                break;
            case "numBases":
                result = numBases;
                break;
            case "timeSinceLastScout":
                result = timeSinceLastScout;
                break;
            case "combatUnitStatuses":
                result = combatUnitStatuses;
                break;
            case "numEnemyWorkers":
                result = numEnemyWorkers;
                break;
            case "numEnemyBases":
                result = numEnemyBases;
                break;
            case "mostCommonCombatUnit":
                result = mostCommonCombatUnit;
                break;
            case "attackingEnemyBase":
                result = attackingEnemyBase;
                break;
            case "beingAttacked":
                result = beingAttacked;
                break;
            case "playerRace":
                result = playerRace;
                break;
            case "enemyRace":
                result = enemyRace;
                break;
            case "gameStatus":
                result = gameStatus;
                break;
            case "trainingCapacity":
                result = trainingCapacity;
                break;
            default:
                result = null;
        }


        return result;
    }


    /**
     * Should make a shallow copy of this object.
     * @return A list of variables that make up this state
     */
    public List variableKeys() {
        return VariableKeys;
    }


    /**
     * @return a copy of this {@link PlanningState}
     */
    public PlanningState copy() {
        PlanningState copy = new PlanningState(this.numWorkers, this.mineralProductionRate,this.gasProductionRate,this.numBases,
                this.timeSinceLastScout, this.combatUnitStatuses, this.numEnemyWorkers,this.numEnemyBases,
                this.mostCommonCombatUnit,this.attackingEnemyBase,this.beingAttacked, this.playerRace, this.enemyRace, this.gameStatus,
                this.trainingCapacity);
        return copy;
    }

    /* ----------- Getters and Setters -------------- */

    public PlanningState s() {
        return this;
    }

    public int getNumWorkers() {
        return numWorkers;
    }

    public int getMineralProductionRate() {
        return mineralProductionRate;
    }

    public int getGasProductionRate() {
        return gasProductionRate;
    }

    public int getNumBases() {
        return numBases;
    }

    public int getTimeSinceLastScout() {
        return timeSinceLastScout;
    }

    public ArrayList<CombatUnitStatus> getCombatUnitStatuses() {
        return combatUnitStatuses;
    }

    public int getArmySize() {
        ArrayList<CombatUnitStatus> combatList = (ArrayList<CombatUnitStatus>) get("combatUnitStatuses");
        int armySum = 0;
        for (CombatUnitStatus combatUnitStatus: combatList) {
            armySum += combatUnitStatus.getAmount();
        }
        return armySum;
    }

    public int getNumEnemyWorkers() {
        return numEnemyWorkers;
    }

    public int getNumEnemyBases() {
        return numEnemyBases;
    }

    public UnitType getMostCommonCombatUnit() {
        return mostCommonCombatUnit;
    }

    public Boolean getAttackingEnemyBase() {
        return attackingEnemyBase;
    }

    public Race getPlayerRace() {
        return playerRace;
    }

    public Race getEnemyRace() {
        return enemyRace;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int[][] getTrainingCapacity() {
        return trainingCapacity;
    }
}

