package Planning;

import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.QValue;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;

/**
 * A bad Q Value class from before we (or at least I) realized that the
 * SparseSampler in burlap already implemented this for us.
 *
 * the goal is to just return a value, in an attempt to solve a bug earlier.
 */
public class DummyQValue implements QFunction {

    /**
     * Returns the {@link QValue} for the given state-action pair.
     *
     * @param s the input state
     * @param a the input action
     * @return the {@link QValue} for the given state-action pair.
     */
    @Override
    public double qValue(State s, Action a) {
        return 0.05;
    }

    /**
     * Returns the value function evaluation of the given state. If the value is not stored, then the default value
     * specified by the ValueFunctionInitialization object of this class is returned.
     *
     * @param s the state to evaluate.
     * @return the value function evaluation of the given state.
     */
    @Override
    public double value(State s) {
        return 0;
    }
}
