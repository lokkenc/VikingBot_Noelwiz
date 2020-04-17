package Planning.Actions;

import Planning.CombatUnitStatus;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class implements the ActionType abstract class from burlap
 * and is used to tell the planner what actions are possible to take at a givens
 * state
 *
 * Why This Exists: To provide the ai planer with possible attacking actions.
 *
 * How to incorporate more options: in AllApplicableActions, add one or more attack
 * actions with the given argument.
 *
 * I recommend doing this with another for loop inside
 * or encapsulating the existing ones to enumerate all the combinations of arguments.
 */
public class AttackActionType implements ActionType {
    /**
     * The String that is the name of this action type.
     */
    private static final String name = "Attack";
    /**
     * An array of all possible arguments that can go in the "what=%s" part of a
     * valid attack action's name. Done like this because one of them going to be
     * valid if the others aren't. Additionally the AI Planner and reward function
     * will decide which one is best, so even though sometimes these are bad options,
     * it's not worth eliminating them here.
     */
    protected static final String[] whatOptions = {"army","base","harass"};

    //documentation should be inherited from burlap
    public String typeName() {
        return name;
    }

    /**
     * Creates an Attack Action based on the given string.
     * @param s A string, the name of an attack action, potentially with
     *          options included
     * @return an AttackAction. All valid arguments are retained, but invalid ones
     * will get dropped by the constructor.
     */
    public Action associatedAction(String s) {
        return new AttackAction(s);
    }

    /**
     * Given a state, enumerate all actions that can be taken by the bot.
     * An important note for this and all similar functions is that the actual game of starcraft
     * may not match this state because this is used by the planner to explore options.
     *
     * why it exists: The BWAPI needs a way to know what action it can take in a given plausable state.
     * This function enumarates all of them based on the state given.
     *
     * how it works: Uses the current state to figure out what attack actions to tell the planner are possible
     * via taking info from the state and creating all combinations.
     *
     * @param state A BWAPI state, assumed to be specifically a PlanningState.
     * @return A List of attack actions that may be taken.
     */
    public List<Action> allApplicableActions(State state) {
        List<Action> attackActions = new ArrayList<Action>();
        attackActions.add(new AttackAction());
        String[] optionset;
        ArrayList<CombatUnitStatus> combatStatus = (ArrayList<CombatUnitStatus>) state.get("combatUnitStatuses");

        //enumarate all valid attack actions
        if (!(boolean) state.get("attackingEnemyBase") && HaveCombatUnits(combatStatus)){
            for (String whatOption : whatOptions) {
                String whatOptStr = "what=".concat(whatOption);

                for (CombatUnitStatus currentUnit : combatStatus) {
                    if (currentUnit.getAmount() > 0 && currentUnit.getCombatUnit().canAttack() && !currentUnit.getCombatUnit().isWorker()) {
                        optionset = new String[]{whatOptStr, "unit=".concat(currentUnit.getCombatUnit().name())};
                        attackActions.add(new AttackAction(optionset));
                    }
                }

                //all option
                optionset = new String[]{whatOptStr, "unit=all"};
                attackActions.add(new AttackAction(optionset));
            }
        }

        //when there is an indication of us being attacked added to the states, add that here
        //TODO: check if the state says we're being attacked.
        if(true){
            //let the bot bring the entire army back to defend
            attackActions.add( new AttackAction( "what=defend_unit=all" ) );
        }

        return attackActions;
    }

    /**
     * Check if we even have combat units to attack with before telling the bot we
     * can attack something.
     * @param botUnits an ArrayList of combat units from a state.
     * @return true if any of our units are for fighting. otherwise false.
     */
    static boolean HaveCombatUnits(ArrayList<CombatUnitStatus> botUnits){
        boolean hasCombat = false;

        for(int i = 0; i < botUnits.size() && !hasCombat; i++){
            CombatUnitStatus current = botUnits.get(i);
            if( (current.getCombatUnit().canAttack()) && (current.getAmount() > 0)
            && !current.getCombatUnit().isWorker()){
                hasCombat = true;
            }
        }

        return hasCombat;
    }
}
