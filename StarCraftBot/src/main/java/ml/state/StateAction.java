package ML.States;

import ML.actions.Action;

/**
 * Represents a (State, Action) pair.
 */
public class StateAction {
    private State state;
    private Action action;

    /**
     * Initialize a (State, Action) pair.
     * @param state specific state.
     * @param action specific action.
     */
    public StateAction(State state, Action action) {
        this.state = state;
        this.action = action;
    }

    /**
     *
     * @return returns the State in the pair.
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @param state sets the state in the pair.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     *
     * @return returns the Action in the pair.
     */
    public Action getAction() {
        return action;
    }

    /**
     *
     * @param action sets the action in the pair.
     */
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