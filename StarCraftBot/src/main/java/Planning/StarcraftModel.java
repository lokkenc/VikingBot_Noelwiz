package Planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;

import java.util.List;

public class StarcraftModel implements FullModel {
    @Override
    public List<TransitionProb> transitions(State state, Action action) {
        return null;
    }

    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        return null;
    }

    @Override
    public boolean terminal(State state) {
        return false;
    }
}
