package ML.Data;

import ML.Actions.Action;
import ML.States.State;
import bwapi.UnitType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class DataManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<DataPoint> dataPoints;
    private HashMap<State, Integer> stateFrequency;
    private UnitType type;

    public DataManager(UnitType type) {
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>();
        this.type = type;
    }

    public DataManager(UnitType type, Set<State> states) {
        dataPoints = new ArrayList<>();
        stateFrequency = new HashMap<>(states.size());
        this.type = type;

        for(State state : states) {
            stateFrequency.put(state, 0);
        }
    }

    public void addDataPoint(DataPoint point) {
        dataPoints.add(point);
    }

    public void addDataPoint(State current, Action action, State next, double reward, double qvalue) {
        dataPoints.add(new DataPoint(current, action, next, reward, qvalue));
    }

    public void addState(State state) {
        if(stateFrequency.containsKey(state)) {
            stateFrequency.put(state, stateFrequency.get(state) + 1);
        } else {
            stateFrequency.put(state, 1);
        }
    }

    public ArrayList<Double> getRewards() {
        ArrayList<Double> rewards = new ArrayList<>();

        for(DataPoint point : dataPoints) {
            rewards.add(point.getReward());
        }

        return rewards;
    }

    public ArrayList<Double> getQvalues() {
        ArrayList<Double> qvalues = new ArrayList<>();

        for(DataPoint point : dataPoints) {
            qvalues.add(point.getQvalue());
        }

        return qvalues;
    }

    public int getFrequency(State state) {
        int frequency = 0;

        if(stateFrequency.containsKey(state)) {
            frequency = stateFrequency.get(state);
        }

        return frequency;
    }

    public int getMaxFrequency() {
        return Collections.max(stateFrequency.values());
    }

    public State getMaxFrequencyState() {
        return Collections.max(stateFrequency.entrySet(), HashMap.Entry.comparingByValue()).getKey();
    }

    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public HashMap<State, Integer> getStateFrequency() {
        return stateFrequency;
    }

    public UnitType getType() {
        return type;
    }
}
