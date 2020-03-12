package src.main.java.ML.States;

import src.main.java.ML.Actions.Action;

public class StateAction {
    private State state;
    private Action action;

    public StateAction(State state, Action action) {
        this.state = state;
        this.action = action;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "StateAction{" +
                "state=" + state +
                ", action=" + action +
                '}';
    }
}