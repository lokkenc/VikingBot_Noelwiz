package Planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.SampleModel;

public class StarcraftSampleModle implements SampleModel {
    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        //TODO: this
        return null;
    }

    @Override
    public boolean terminal(State state) {
        return false;
    }
}
