package ML.Data;

import ML.Actions.Action;
import ML.States.State;

import java.io.Serializable;
import java.util.Objects;

public class DataPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    private State current;
    private Action action;
    private State next;
    private double reward;
    private double qvalue;

    public DataPoint(State current, Action action, State next, double reward, double qvalue) {
        this.current = current;
        this.action = action;
        this.next = next;
        this.reward = reward;
        this.qvalue = qvalue;
    }

    public State getCurrent() {
        return current;
    }

    public Action getAction() {
        return action;
    }

    public State getNext() {
        return next;
    }

    public double getReward() {
        return reward;
    }

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
