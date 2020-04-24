package ml.state;

import ml.actions.*;
import ml.range.*;

import java.util.*;

/**
 * Manages the state space and available actions.
 */
public class StateSpaceManager {
    private List<Action> actionList;
    private Set<State> stateSet;

    /**
     * Initialize the StateSpaceManager
     */
    public StateSpaceManager() {
        this.actionList = getValidActions();
        this.stateSet = createStates();
    }

    /**
     *
     * @return returns a List of valid actions.
     */
    public List<Action> getValidActions() {
        return Arrays.asList(new Attack(), new Retreat(), new MoveTowards(), new GoHome());
    }

    /**
     * Initializes the entire state space for every possible combination of State fields.
     * @return returns a Set containing all possible states.
     */
    public Set<State> createStates() {
        Set<State> states = new HashSet<State>();
        boolean onCoolDown = false;
        boolean skirmish = false;

        // Create all the states here
        for(int i = 0; i < 2; i++) { // true vs false onCoolDown value
            for(int j = 0; j < 2; j++) { // true vs false skirmish value
                for(DistanceRange dRange: DistanceRange.values()) {
                    for (UnitsRange ueRange : UnitsRange.values()) {
                        for (UnitsRange ufRange : UnitsRange.values()) {
                            for (HpRange ehpRange : HpRange.values()) {
                                for (HpRange fhpRange : HpRange.values()) {
                                    states.add(new State(onCoolDown, new Distance(dRange), new Units(ueRange), new Units(ufRange), new Hp(ehpRange), new Hp(fhpRange), skirmish, null));
                                }
                            }
                        }
                    }
                }
                skirmish = true;
            }
            onCoolDown = true;
        }
        return states;
    }

    /**
     *
     * @return returns a List of the valid actions.
     */
    public List<Action> getActionList() {
        return actionList;
    }

    /**
     *
     * @return returns the Set of all States.
     */
    public Set<State> getStateSet() {
        return stateSet;
    }
}
