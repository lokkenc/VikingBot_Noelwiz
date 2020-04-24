package ml.data;

import ml.actions.Action;
import ml.state.State;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents relevant information such as current State, executed Action, next State, produced reward, and Q-Value.
 */
public class DataPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    private final State current;
    private final Action action;
    private final State next;
    private final double reward;
    private final double qvalue;

    /**
     * Initialize the DataPoint with relative information such as the current State, executed Action, next State,
     * reward received, and the calculated Q-Value.
     * @param current the current State.
     * @param action the Action executed in the current State.
     * @param next the resulting State from executing the Action in the current State.
     * @param reward the reward received for executing the Action in the current State.
     * @param qvalue the calculated Q-Value for the (State, Action, State, reward) tuple.
     */
    public DataPoint(State current, Action action, State next, double reward, double qvalue) {
        this.current = current;
        this.action = action;
        this.next = next;
        this.reward = reward;
        this.qvalue = qvalue;
    }

    /**
     * Get the current State.
     * @return returns the current State.
     */
    public State getCurrent() {
        return current;
    }

    /**
     * Get the Action executed in the current State.
     * @return returns the Action executed in the current State.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Gets the resulting next State from executing the Action in the current State.
     * @return returns the resulting next State.
     */
    public State getNext() {
        return next;
    }

    /**
     * Gets the reward given for executing the Action in the current State.
     * @return returns a reward in the form of a double for executing the Action in the current State.
     */
    public double getReward() {
        return reward;
    }

    /**
     * Gets the Q-Value associated with the (State, Action, State, reward) tuple.
     * @return returns the Q-Value for the (State, Action, State, reward) tuple.
     */
    public double getQvalue() {
        return qvalue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPoint dataPoint = (DataPoint) o;
        return Double.compare(dataPoint.reward, reward) == 0 &&
                Double.compare(dataPoint.qvalue, qvalue) == 0 &&
                Objects.equals(current, dataPoint.current) &&
                Objects.equals(action, dataPoint.action) &&
                Objects.equals(next, dataPoint.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, action, next, reward, qvalue);
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "current=" + current +
                ", action=" + action +
                ", next=" + next +
                ", reward=" + reward +
                ", qvalue=" + qvalue +
                '}';
    }
}
