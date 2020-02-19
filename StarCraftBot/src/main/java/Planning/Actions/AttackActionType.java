package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class AttackActionType implements ActionType {
    private static final String name = "Attack";
    protected static final String[] groupOptions = {"all","bot", "selected"};
    protected static final String[] amountOptions = {"all","half"};
    protected static final String[] whatOptions = {"army","base","harass"};


    public String typeName() {
        return name;
    }

    public Action associatedAction(String s) {
        return new AttackAction(s);
    }

    public List<Action> allApplicableActions(State state) {
        //TODO: incorperate state knowns to add to this list
        List<Action> attackActions = new ArrayList<Action>();
        attackActions.add(new AttackAction());
        String[] optionset;

        for(int amountop = 0; amountop < amountOptions.length; amountop++){
            String amountOptStr = "amount=".concat(amountOptions[amountop]);

            for(int whatop = 0; whatop < whatOptions.length; whatop++){
                String whatOptStr = "what=".concat(whatOptions[whatop]);

                for (int groupop = 0; groupop < groupOptions.length; groupop++){
                    String groupOptStr = "group=".concat(groupOptions[groupop]);

                    //all combos of options with all 3 filled out
                    optionset = new String[]{groupOptStr, amountOptStr, whatOptStr};
                    attackActions.add(new AttackAction(optionset));
                }

                //all combos of just the what option filled out.
                optionset = new String[]{whatOptStr};
                attackActions.add(new AttackAction(optionset));
            }
        }
        //missing combinations: any attacks with just group, or just amount, and combinations of 2/3 options
        //I don't think those would be useful compared to the complexity they'd add right now.


        return attackActions;
    }
}
