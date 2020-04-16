package ML.Data;

import ML.Actions.Action;
import ML.States.State;
import bwapi.UnitType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Manages the collection of relevant data in the form of DataPoints.
 */
public class DataManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<DataPoint> dataPoints;
    private final HashMap<State, Integer> stateFrequency;
    private final UnitType type;

    /**
     * Initializes the DataManager with the type of unit it's collecting data on.
     * @param type the type of unit.
     */
    public DataManager(UnitType type) {
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>();
        this.type = type;
    }

    /**
     * Initializes the DataManager with the type of unit it's collecting data on as well as all possible States in the
     * State space.
     * @param type the type of unit.
     * @param states the set of all possible States in the State space.
     */
    public DataManager(UnitType type, Set<State> states) {
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>(states.size());
        this.type = type;

        for(State state : states) {
            stateFrequency.put(state, 0);
        }
    }

    /**
     * Adds a DataPoint to collection of DataPoints being stored within the DataManager.
     * @param point the DataPoint to be added.
     */
    public void addDataPoint(DataPoint point) {
        dataPoints.add(point);
    }

    /**
     * Constructs and adds a DataPoint to the collection of DataPoints being stored within the DataManager.
     * @param current the current State.
     * @param action the Action executed in the current State.
     * @param next the resulting next State from executing the Action in the current State.
     * @param reward the produced reward for executing the Action in the current State.
     * @param qvalue the calculated Q-Value for the (State, Action, State, reward) tuple.
     */
    public void addDataPoint(State current, Action action, State next, double reward, double qvalue) {
        dataPoints.add(new DataPoint(current, action, next, reward, qvalue));
    }

    /**
     * Add a State to the stateFrequency map.
     * @param state the State to add to the stateFrequency map.
     */
    public void addState(State state) {
        if(stateFrequency.containsKey(state)) {
            stateFrequency.put(state, stateFrequency.get(state) + 1);
        } else {
            stateFrequency.put(state, 1);
        }
    }

    /**
     * Gets an ArrayList of rewards from the collection of DataPoints.
     * @return returns an ArrayList of doubles containing all of the rewards that have been received.
     */
    public ArrayList<Double> getRewards() {
        ArrayList<Double> rewards = new ArrayList<>();

        for(DataPoint point : dataPoints) {
            rewards.add(point.getReward());
        }

        return rewards;
    }

    /**
     * Gets an ArrayList of Q-Values from the collection of DataPoints.
     * @return returns an ArrayList of doubles containing all of the Q-Values that have been calculated.
     */
    public ArrayList<Double> getQvalues() {
        ArrayList<Double> qvalues = new ArrayList<>();

        for(DataPoint point : dataPoints) {
            qvalues.add(point.getQvalue());
        }

        return qvalues;
    }

    /**
     * Gets the integer frequency of a specific State.
     * @param state the State used to find the number of times that the State has been visited.
     * @return returns an integer value representing the number of times that the State has been visited.
     */
    public int getFrequency(State state) {
        int frequency = 0;

        if(stateFrequency.containsKey(state)) {
            frequency = stateFrequency.get(state);
        }

        return frequency;
    }

    /**
     * Returns the largest frequency in the stateFrequency map.
     * @return returns an integer value representing the maximum number of times any particular State has been visited.
     */
    public int getMaxFrequency() {
        return Collections.max(stateFrequency.values());
    }

    /**
     * Gets the State that has the largest frequency.
     * @return returns the State that has the maximum number of occurrences in the stateFrequency map.
     */
    public State getMaxFrequencyState() {
        return Collections.max(stateFrequency.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    /**
     * Gets an ArrayList of all of the DataPoints that have been collected.
     * @return returns an ArrayList of all collected DataPoints.
     */
    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    /**
     * Gets the stateFrequency map.
     * @return returns a HashMap of State to int which represents how many times a unique state has been visited.
     */
    public HashMap<State, Integer> getStateFrequency() {
        return stateFrequency;
    }

    /**
     * Gets the type of unit that the DataManager is collecting data on.
     * @return returns the type of unit that the DataManager is collecting data on.
     */
    public UnitType getType() {
        return type;
    }
}
