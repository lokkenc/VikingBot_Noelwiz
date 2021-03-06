package ml.data;

import ml.actions.Action;
import ml.actions.ActionType;
import ml.model.UnitClassification;
import ml.state.State;

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
    private final UnitClassification unitClass;

    /**
     * Initializes the DataManager with the type of unit it's collecting data on.
     * @param unitClass the units classification as one of the following: Melee, Ranged
     */
    public DataManager(UnitClassification unitClass) {
        this.unitClass = unitClass;
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>();
    }

    /**
     * Initializes the DataManager with the type of unit it's collecting data on as well as all possible States in the
     * State space.
     * @param unitClass the units classification as one of the following: Melee, Ranged
     * @param states the set of all possible States in the State space.
     */
    public DataManager(UnitClassification unitClass, Set<State> states) {
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>(states.size());
        this.unitClass = unitClass;

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
     * @return An ArrayList of doubles containing all of the rewards that have been received.
     */
    public ArrayList<Double> getRewards() {
        ArrayList<Double> rewards = new ArrayList<>();

        for(DataPoint point : dataPoints) {
            rewards.add(point.getReward());
        }

        return rewards;
    }

    /**
     * Gets the average of all of the rewards that have been collected.
     * @return A double of the average over all collected rewards.
     */
    public double getAverageReward() {
        double avg = 0;

        for(DataPoint point : dataPoints) {
            avg += point.getReward();
        }

        if(!dataPoints.isEmpty()) {
            avg /= dataPoints.size();
        }

        return avg;
    }

    /**
     * Gets the average of all rewards given to a specific ActionType.
     * @param type the ActionType for filtering rewards.
     * @return A double of the average over all collected rewards for the specified ActionType.
     */
    public double getAverageReward(ActionType type) {
        double avg = 0;

        for(DataPoint point : dataPoints) {
            if(point.getAction().getType() == type) {
                avg += point.getReward();
            }
        }

        if(!dataPoints.isEmpty()) {
            avg /= dataPoints.size();
        }

        return avg;
    }

    /**
     * Gets an ArrayList of Q-Values from the collection of DataPoints.
     * @return An ArrayList of doubles containing all of the Q-Values that have been calculated.
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
     * @return An integer value representing the number of times that the State has been visited.
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
     * @return An integer value representing the maximum number of times any particular State has been visited.
     */
    public int getMaxFrequency() {
        return Collections.max(stateFrequency.values());
    }

    /**
     * Gets the State that has the largest frequency.
     * @return The State that has the maximum number of occurrences in the stateFrequency map.
     */
    public State getMaxFrequencyState() {
        return Collections.max(stateFrequency.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    /**
     * Gets an ArrayList of all of the DataPoints that have been collected.
     * @return An ArrayList of all collected DataPoints.
     */
    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    /**
     * Gets the stateFrequency map.
     * @return A HashMap of State to int which represents how many times a unique state has been visited.
     */
    public HashMap<State, Integer> getStateFrequency() {
        return stateFrequency;
    }

    /**
     * Gets the unitClassification of unit that the DataManager is collecting data on.
     * @return The classification of the unit we are gathering data on
     */
    public UnitClassification getClassification() {
        return unitClass;
    }
}
