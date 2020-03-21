package ML.States;

import ML.Actions.*;
import ML.Range.*;

import java.util.*;

public class StateSpaceManager {
    private List<Action> actionList;
    private Set<State> stateSet;

    public StateSpaceManager() {
        this.actionList = getValidActions();
        this.stateSet = createStates();
    }

    public List<Action> getValidActions() {
        return Arrays.asList(new Attack(), new Retreat(), new MoveTowards());
    }

    public Set<State> createStates() {
        Set<State> states = new HashSet<State>();
        boolean onCoolDown = false;

        // Create all the states here
        for(int i = 0; i < 2; i++) {
            for(DistanceRange dRange: DistanceRange.values()) {
                for(UnitsRange ueRange: UnitsRange.values()) {
                    for(UnitsRange ufRange: UnitsRange.values()) {
                        for (HpRange ehpRange : HpRange.values()) {
                            for (HpRange fhpRange : HpRange.values()) {
                                states.add(new State(onCoolDown, new Distance(dRange), new Units(ueRange), new Units(ufRange), new Hp(ehpRange), new Hp(fhpRange)));
                            }
                        }
                    }
                }
            }
            onCoolDown = true;
        }
        return states;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public Set<State> getStateSet() {
        return stateSet;
    }
}
