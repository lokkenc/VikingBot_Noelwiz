package ML.Data;

import ML.States.State;

import java.util.Objects;

public class DataPoint {
    private State current;
    private double reward;
    private double qvalue;
    private State next;

    public DataPoint(State current, double reward, double qvalue, State next) {
        this.current = current;
        this.reward = reward;
        this.qvalue = qvalue;
        this.next = next;
    }

    public State getCurrent() {
        return current;
    }

    public double getReward() {
        return reward;
    }

    public double getQvalue() {
        return qvalue;
    }

    public State getNext() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPoint dataPoint = (DataPoint) o;
        return Double.compare(dataPoint.reward, reward) == 0 &&
                Double.compare(dataPoint.qvalue, qvalue) == 0 &&
                Objects.equals(current, dataPoint.current) &&
                Objects.equals(next, dataPoint.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, reward, qvalue, next);
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "current=" + current +
                ", reward=" + reward +
                ", qvalue=" + qvalue +
                ", next=" + next +
                '}';
    }
}
