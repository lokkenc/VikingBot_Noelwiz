package ML.Learning;

import ML.Actions.Action;
import ML.Data.DataManager;
import ML.Model.UnitClassification;
import bwapi.UnitType;
import ML.States.*;

import java.io.File;
import java.io.Serializable;

/**
 * Manages the learning model, state space, and action chooser.
 */
public class LearningManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UnitClassification unitClass;
    private final StateSpaceManager spaceManager;
    private final SARSA sarsa;
    private final GreedyActionChooser greedyActionChooser;

    /**
     * Initialize the LearningManager, set the type of unit it's managing, create the state space, create the learning
     * algorithm, and create the GreedyActionChooser.
     * @param unitClass Units classification into one of the following options: Melee, Ranged
     */
    public LearningManager(UnitClassification unitClass) {
        this.unitClass = unitClass;
        spaceManager = new StateSpaceManager();
        sarsa = new SARSA(unitClass, spaceManager);
        greedyActionChooser = new GreedyActionChooser();
    }

    /**
     * Load the QTable from disk if it exists.
     */
    public void loadQTable() {
        File f = new File("TrainingFiles/Tables/" + unitClass.toString() + "Table.ser");
        if(f.exists()) {
            sarsa.loadQTable();
        }
    }

    /**
     * Store the QTable to disk.
     */
    public void storeQTable() {
        sarsa.storeQTable();
    }

    /**
     * Load the DataManager from disk if it exists.
     */
    public void loadDataManager() {
        File f = new File("TrainingFiles/Tables/" + unitClass.toString() + "Data.ser");
        if(f.exists()) {
            sarsa.loadDataManager();
        }
    }

    /**
     * Store the DataManager.
     */
    public void storeDataManager() {
        sarsa.storeDataManager();
    }

    /**
     * Update the learning algorithm with the current State, the Action executed, and the resulting next State.
     * @param current the current State.
     * @param action the Action executed in the current State.
     * @param next the resulting next State from the (current, action) pair.
     */
    public void updateState(State current, Action action, State next) {
        sarsa.updateQTable(current, action, next);
    }

    /**
     * Uses the GreedyActionChooser to get the action that should be executed in the current State.
     * @param state the current State.
     * @return returns the Action that should be executed in the current State.
     */
    public Action getNextAction(State state) {
        return greedyActionChooser.chooseAction(spaceManager, state, sarsa.getQTable());
    }

    /**
     * Gets the type of unit the LearningManager is managing.
     * @return returns the classification of the unit being managed
     */
    public UnitClassification getUnitClassification() { return this.unitClass; }

    /**
     * Gets the DataManager being used by the LearningManager for collecting data points.
     * @return returns the DataManager.
     */
    public DataManager getDataManager() {
        return sarsa.getDataManager();
    }
}
