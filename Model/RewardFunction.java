import Actions.Action;
import bwapi.Unit;

import java.util.ArrayList;

public class RewardFunction {
    public static final double DEFAULT_REWARD = 10.0;

    /**
     * This function returns the reward to be given based on the action taken and the difference in states
     * @param current The state before the action is taken
     * @param next The state after the action is taken
     * @param action The action that was performed
     * @return A Double that contains the value of the reward
     */
    public static double getRewardValue(State current, State next, Action action) {
        double reward = DEFAULT_REWARD;
        ArrayList<Unit> allFriendlyUnits = current.getFriendlyUnits().getUnits();
        ArrayList<Unit> allEnemyUnits = current.getEnemyUnits().getUnits();
        Unit centralUnit  = current.getFriendlyUnits().getCentralUnit();

        switch (action.getType()) {
            case ATTACK:
                // Reward attacking along the lines of
                int currentFriendlyTotalHealth = 0;
                int currentEnemyTotalHealth = 0;

                for(Unit unit: allFriendlyUnits) { // get total friendly units health if they were max
                    currentFriendlyTotalHealth += unit.getInitialHitPoints();
                }

                for(Unit unit: allEnemyUnits) { // get total enemy units health if they were max
                    currentEnemyTotalHealth += unit.getInitialHitPoints();
                }

                /* this function looks like a mess in java code so here it is in simpler format
                   ((totalDamageDoneToEnemy * friendlyUnitsDamage) - ((totalEnemyHealth / totalFriendlyHealth) * totalDamageDoneToFriendly)) / 20
                   the division at the end is done to make the reward factor closer to the other rewards since without dividing
                   it could potentially be giving rewards in the 10's and maybe 100's if there was enough damage done */
               reward = (((current.getEnemyHitPoints() - next.getEnemyHitPoints()) *
                        (allFriendlyUnits.get(0).getType().groundWeapon().damageAmount() + allFriendlyUnits.get(0).getType().airWeapon().damageAmount())) -
                        ((currentEnemyTotalHealth / currentFriendlyTotalHealth) *
                        (current.getFriendlyHitPoints() - next.getFriendlyHitPoints()))) / 20;
                break;
            case MOVEDOWNLEFT: // we only want to reward movement if it is towards a friendly or enemy unit
                boolean movingTowards = false;
                for(Unit unit: allFriendlyUnits) { // for all friendlies
                    if(unit.getX() < centralUnit.getX() && unit.getY() < centralUnit.getY()) { // there is a unit down/left
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allFriendlyUnits) { // for all enemies only if we haven't already found a friendly there
                        if(unit.getX() < centralUnit.getX() && unit.getY() < centralUnit.getY()) { // there is a unit down/left
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) { // if they aren't moving towards a unit then give a negative reward to promote not doing this
                    reward = -0.5;
                }
                break;
            case MOVEDOWNRIGHT: // every one of the move cases has the same logic but for the direction of movement specified
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if(unit.getX() > centralUnit.getX() && unit.getY() < centralUnit.getY()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allFriendlyUnits) {
                        if (unit.getX() > centralUnit.getX() && unit.getY() < centralUnit.getY()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVEUP:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if((centralUnit.getX() - 7 < unit.getX() && unit.getX() < centralUnit.getX() + 7) && unit.getY() > centralUnit.getY()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allEnemyUnits) {
                        if ((centralUnit.getX() - 7 < unit.getX() && unit.getX() < centralUnit.getX() + 7) && unit.getY() > centralUnit.getY()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVEUPLEFT:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if(unit.getX() < centralUnit.getX() && unit.getY() < centralUnit.getY()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allFriendlyUnits) {
                        if(unit.getX() < centralUnit.getX() && unit.getY() < centralUnit.getY()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVEUPRIGHT:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if(unit.getX() > centralUnit.getX() && unit.getY() > centralUnit.getY()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allFriendlyUnits) {
                        if (unit.getX() > centralUnit.getX() && unit.getY() > centralUnit.getY()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVEDOWN:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if((centralUnit.getX() - 7 < unit.getX() && unit.getX() < centralUnit.getX() + 7) && unit.getY() < centralUnit.getY()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allEnemyUnits) {
                        if ((centralUnit.getX() - 7 < unit.getX() && unit.getX() < centralUnit.getX() + 7) && unit.getY() < centralUnit.getY()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVERIGHT:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if((centralUnit.getY() - 7 < unit.getX() && unit.getY() < centralUnit.getY() + 7) && unit.getX() > centralUnit.getX()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allEnemyUnits) {
                        if ((centralUnit.getY() - 7 < unit.getY() && unit.getY() < centralUnit.getY() + 7) && unit.getX() > centralUnit.getX()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case MOVELEFT:
                movingTowards = false;
                for(Unit unit: allFriendlyUnits) {
                    if((centralUnit.getY() - 7 < unit.getX() && unit.getY() < centralUnit.getY() + 7) && unit.getX() < centralUnit.getX()) {
                        movingTowards = true;
                        break;
                    }
                }

                if(!movingTowards) {
                    for (Unit unit : allEnemyUnits) {
                        if ((centralUnit.getY() - 7 < unit.getY() && unit.getY() < centralUnit.getY() + 7) && unit.getX() < centralUnit.getX()) {
                            movingTowards = true;
                            break;
                        }
                    }
                }

                if(!movingTowards) {
                    reward = -0.5;
                }
                break;
            case SPREAD:
                boolean giveReward = false;
                for(Unit unit: allEnemyUnits) { // if any enemy units do any kind of splash damage reward this
                    if(unit.getType().groundWeapon().innerSplashRadius() != 0 || unit.getType().airWeapon().innerSplashRadius() != 0) {
                        giveReward = true;
                        break;
                    }
                }

                if(giveReward) {
                    reward = 0.5;
                } else {
                    reward = -0.5;
                }
                break;
        }

        if(allFriendlyUnits.size() < next.getFriendlyUnits().getUnits().size()) {
            // if we have lost a unit lower the reward even more to promote surviving
            reward -= 10;
        }
        return reward;
    }
}