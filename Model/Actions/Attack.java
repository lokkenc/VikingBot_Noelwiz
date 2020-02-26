package Actions;

import Actions.ActionType;

public class Attack extends Action {
    private ActionType type = ATTACK;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the units to attack the enemy with the lowest health in range
     * @param game The game that was initialized at the startup of the program
     * @param units The group of units that makes up the commandable squad
     */
    public void doAction(Game game, Units units){
        
    }
}