package Planning;

import burlap.mdp.core.state.State;
import burlap.statehashing.HashableState;
import bwapi.*;

import java.util.ArrayList;
import java.util.List;

enum GameStatus {
    EARLY, MID, LATE;
}

public class PlanningState implements HashableState, State {
    private int numWorkers;
    private int mineralProductionRate;
    private int gasProductionRate;
    private int numBases;
    private int timeSinceLastScout;
    private ArrayList<CombatUnitStatus> combatUnitStatuses = new ArrayList<CombatUnitStatus>();
    private int numEnemyWorkers;
    private int numEnemyBases;
    private Unit mostCommonCombatUnit;
    private Boolean attackingEnemyBase;
    private Race playerRace;
    private Race enemyRace;
    private GameStatus gameStatus;
    private int[][] trainingCapacity; /*[ workers[used, avail] , ground[used, avail], combat air[used, avail], support air[used, avail]] */

    private ArrayList<String> VariableKeys = new ArrayList<String>();


    public PlanningState(int numWorkers, int mineralProductionRate, int gasProductionRate, int numBases, int timeSinceLastScout,
                         ArrayList<CombatUnitStatus> combatUnitStatuses, int numEnemyWorkers, int numEnemyBases, Unit mostCommonCombatUnit,
                         Boolean attackingEnemyBase, Race playerRace, Race enemyRace, GameStatus gameStatus, int[][] trainingCapacity) {
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
        this.playerRace = playerRace;
        this.enemyRace = enemyRace;
        this.gameStatus = gameStatus;
        this.trainingCapacity = trainingCapacity;

        VariableKeys = new ArrayList<String>();
        variableKeys().add("numWorkers");
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
        switch (key) {
            case "numWorkers":
                result = numWorkers;
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    /**
     * Should make a shallow copy of this object.
     * @return an object that is different than this obejct with the
     *         same values
     */
    public PlanningState copy() {
        return this;
    }

    /**
     * Gets a list of all variable keys
     * @return a List of variable keys.
     */
    public List variableKeys() {
        return VariableKeys;
    }



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

    public int getNumEnemyWorkers() {
        return numEnemyWorkers;
    }

    public int getNumEnemyBases() {
        return numEnemyBases;
    }

    public Unit getMostCommonCombatUnit() {
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

