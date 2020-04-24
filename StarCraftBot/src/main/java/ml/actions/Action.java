package ml.actions;

import bwapi.Game;
import bwapi.Unit;

import java.io.Serializable;

/**
 * Abstract class for functions that are needed for all actions
 */
public abstract class Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type;

    /**
     * Get the type of the action
     * @return returns the type of the action
     */
    public abstract ActionType getType();

    /**
     * Command Unit to take the action
     * @param game Active game stored inside of StrategyAgent
     * @param unit Unit that needs to be given a command
     */
    public abstract void doAction(Game game, Unit unit);

    /**
     * Check for equality between another object
     * @param o Object to check for equality
     * @return True if object and action are equal, false otherwise
     */
    public abstract boolean equals(Object o);

    /**
     * Generate a hash for hashing into QTable
     * @return hashCode used to hash into the QTable
     */
    public abstract int hashCode();

    /**
     * Override of the toString function to format output in the desired fashion
     * @return String containing Action information
     */
    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                '}';
    }
}
