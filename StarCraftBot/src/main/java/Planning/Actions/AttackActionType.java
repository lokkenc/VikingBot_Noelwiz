package Planning.Actions;

import Planning.CombatUnitStatus;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class AttackActionType implements ActionType {
    private static final String name = "Attack";
    protected static final String[] whatOptions = {"army","base","harass"};

    public String typeName() {
        return name;
    }

    public Action associatedAction(String s) {
        return new AttackAction(s);
    }

    public List<Action> allApplicableActions(State state) {
        List<Action> attackActions = new ArrayList<Action>();
        attackActions.add(new AttackAction());
        String[] optionset;
        ArrayList<CombatUnitStatus> combatStatus = (ArrayList<CombatUnitStatus>) state.get("combatUnitStatuses");

        if (!(boolean) state.get("attackingEnemyBase") && HaveCombatUnits(combatStatus)){
            for (int whatop = 0; whatop < whatOptions.length; whatop++) {
                String whatOptStr = "what=".concat(whatOptions[whatop]);

                for(int unitStatusi = 0; unitStatusi < combatStatus.size(); unitStatusi++){
                    CombatUnitStatus currentUnit = combatStatus.get(unitStatusi);
                    if(currentUnit.getAmount() > 0 && currentUnit.getCombatUnit().canAttack() && !currentUnit.getCombatUnit().getType().isWorker()){
                        optionset = new String[]{whatOptStr, "unit=".concat(currentUnit.getCombatUnit().getType().name()) };
                        attackActions.add(new AttackAction(optionset));
                    }
                }

                //all option
                optionset = new String[]{whatOptStr, "unit=all" };
                attackActions.add(new AttackAction(optionset));
            }
        }

        //when there is an indication of us being attacked added to the states, add that here
        if(true){
            attackActions.add( new AttackAction( "what=defend_unit=all" ) );
        }

        return attackActions;
    }

    static boolean HaveCombatUnits(ArrayList<CombatUnitStatus> botUnits){
        boolean hasCombat = false;

        for(int i = 0; i < botUnits.size() && !hasCombat; i++){
            CombatUnitStatus current = botUnits.get(i);
            if( (current.getCombatUnit().canAttack()) && (current.getAmount() > 0)
            && !current.getCombatUnit().getType().isWorker()){
                hasCombat = true;
            }
        }

        return hasCombat;
    }
}
