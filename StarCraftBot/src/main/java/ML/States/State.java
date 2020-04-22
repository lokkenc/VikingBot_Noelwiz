package ML.States;

import ML.Range.*;

import java.io.Serializable;
import java.util.Objects;

import static ML.Range.UnitsRange.*;

/**
 * Represents a state with all relevant features relating to micromanaging combat.
 */
public class State implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean onCoolDown;
    private final Distance closestEnemy;
    private final Units numberOfEnemies;
    private final Units numberOfFriendlies;
    private final Hp enemyHp;
    private final Hp friendlyHp;
    private final boolean skirmish;

    /**
     * Initializes the state given the current information around a specific unit.
     * @param onCoolDown is the unit's weapon on cool down?
     * @param closestEnemy the distance to the closest enemy.
     * @param numberOfEnemies the total number of enemy units nearby.
     * @param numberOfFriendlies the total number friendly units nearby.
     * @param enemyHp the total HP of nearby enemies.
     * @param friendlyHp the total HP of nearby allies.
     * @param skirmish whether units should stop fighting or not
     */
    public State(boolean onCoolDown, Distance closestEnemy, Units numberOfEnemies, Units numberOfFriendlies, Hp enemyHp, Hp friendlyHp, boolean skirmish) {
        this.onCoolDown = onCoolDown;
        this.closestEnemy = closestEnemy;
        this.numberOfEnemies = numberOfEnemies;
        this.numberOfFriendlies = numberOfFriendlies;
        this.enemyHp = enemyHp;
        this.friendlyHp = friendlyHp;
        this.skirmish = skirmish;
    }

    /**
     *
     * @return returns if the unit's weapon is on cool down.
     */
    public boolean isOnCoolDown() {
        return onCoolDown;
    }

    /**
     *
     * @return returns the distance to the closest enemy.
     */
    public Distance getClosestEnemy() {
        return closestEnemy;
    }

    /**
     *
     * @return returns the number of enemy units nearby.
     */
    public Units getNumberOfEnemies() {
        return numberOfEnemies;
    }

    /**
     *
     * @return returns the number of friendly units nearby.
     */
    public Units getNumberOfFriendlies() {
        return numberOfFriendlies;
    }

    /**
     *
     * @return returns the total HP of nearby enemy units.
     */
    public Hp getEnemyHp() {
        return enemyHp;
    }

    /**
     *
     * @return returns the total HP of nearby friendly units.
     */
    public Hp getFriendlyHp() {
        return friendlyHp;
    }

    /**
     *
     * @return returns the value of the goHome command
     */
    public boolean getSkirmish() { return skirmish; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return onCoolDown == state.onCoolDown &&
                Objects.equals(closestEnemy, state.closestEnemy) &&
                Objects.equals(numberOfEnemies, state.numberOfEnemies) &&
                Objects.equals(numberOfFriendlies, state.numberOfFriendlies) &&
                Objects.equals(enemyHp, state.enemyHp) &&
                Objects.equals(friendlyHp, state.friendlyHp);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((closestEnemy == null) ? 0 : closestEnemy.hashCode());
        result = prime * result + ((friendlyHp == null) ? 0 : friendlyHp.hashCode());
        result = prime * result + ((enemyHp == null) ? 0 : enemyHp.hashCode());
        result = prime * result + ((numberOfFriendlies == null) ? 0 : numberOfFriendlies.hashCode());
        result = prime * result + ((numberOfEnemies == null) ? 0 : numberOfEnemies.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "State{" +
                "onCoolDown=" + onCoolDown +
                ", closestEnemy=" + closestEnemy +
                ", numberOfEnemies=" + numberOfEnemies +
                ", numberOfFriendlies=" + numberOfFriendlies +
                ", enemyHp=" + enemyHp +
                ", friendlyHp=" + friendlyHp +
                '}';
    }

    /**
     * Creates a condensed version of the toString() method.
     * @return returns a condensed version of the toString() method.
     */
    public String condensedString() {
        return "State{" +
                "Enemies (" + numberOfEnemies.getValue() + " units" +
                ", " + enemyHp.getValue() + " hp)" +
                ", Friendlies (" + numberOfFriendlies.getValue() + " units" +
                ", " + friendlyHp.getValue() + " hp)" +
                '}';
    }
}