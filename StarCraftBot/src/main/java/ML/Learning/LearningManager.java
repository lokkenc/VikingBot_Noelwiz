package ML.Learning;

import ML.Actions.Action;
import bwapi.UnitType;
import ML.States.*;

public class LearningManager {

    private UnitType type;
    private StateSpaceManager spaceManager;
    private SARSA sarsa;
    private GreedyActionChooser greedyActionChooser;

    public LearningManager(UnitType type) {
        this.type = type;
        spaceManager = new StateSpaceManager();
        sarsa = new SARSA(type, spaceManager);
        greedyActionChooser = new GreedyActionChooser(sarsa.getQTable());
    }

    public void updateState(State current, Action action, State next) {
        sarsa.updateQTable(current, action, next);
    }

    public Action getNextAction(State state) {
        return greedyActionChooser.chooseAction(spaceManager, state);
    }

    public UnitType getUnitType() { return this.type; }

    /*
     * Should we leave the state transitioning to the CombatAgent and only interact
     * with the learning through updateState and getNextAction?
     */
//    public void setCurrentStateAction(State state, Game game) {
//        Action action = greedy.chooseAction(spManager, state);
//        current.setState(state);
//        current.setAction(action);
//        action.doAction(game, state.getUnit());
//    }
//
//    public void setNextStateAction(State state, Game game) {
//        Action action = greedy.chooseAction(spManager, state);
//        next.setState(state);
//        next.setAction(action);
//    }
}
