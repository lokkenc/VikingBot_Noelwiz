package src.main.java.ML.States;

import src.main.java.ML.Range.*;
import src.main.java.ML.model.TerrainSector;

import java.io.Serializable;
import java.util.Objects;

import static src.main.java.ML.Range.UnitsRange.*;

public class State implements Serializable {
    private static final long serialVersionUID = 7588180712283449263L;
    private boolean onCoolDown;
    private Distance closestEnemy;
    private Units numberOfEnemies;
    private Units numberOfFriendlies;
    private Hp enemyHp;
    private Hp friendlyHp;

    public State(boolean onCoolDown, Distance closestEnemy, Units numberOfEnemies, Units numberOfFriendlies, Hp enemyHp, Hp friendlyHp) {
        this.onCoolDown = onCoolDown;
        this.closestEnemy = closestEnemy;
        this.numberOfEnemies = numberOfEnemies;
        this.numberOfFriendlies = numberOfFriendlies;
        this.enemyHp = enemyHp;
        this.friendlyHp = friendlyHp;
    }

    public boolean isOnCoolDown() {
        return onCoolDown;
    }

    public Distance getClosestEnemy() {
        return closestEnemy;
    }

    public Units getNumberOfEnemies() {
        return numberOfEnemies;
    }

    public Units getNumberOfFriendlies() {
        return numberOfFriendlies;
    }

    public Hp getEnemyHp() {
        return enemyHp;
    }

    public Hp getFriendlyHp() {
        return friendlyHp;
    }

    public int getKeyVal(State state) {
        int keyVal = 0;

        if(state.isOnCoolDown()) {
            keyVal+=1;
        } else {
            keyVal+=2;
        }

        if(state.getNumberOfEnemies().getRange() == SMALL) {
            keyVal+=3;
        } else if(state.getNumberOfEnemies().getRange() == MEDIUM) {
            keyVal+=4;
        } else if(state.getNumberOfEnemies().getRange() == LARGE) {
            keyVal+=5;
        }

        if(state.getFriendlyHp().getRange() == HpRange.LOW) {
            keyVal+=6;
        } else if(state.getFriendlyHp().getRange() == HpRange.MEDIUM_LOW) {
            keyVal+=7;
        } else if(state.getFriendlyHp().getRange() == HpRange.MEDIUM_HIGH) {
            keyVal+=8;
        } else {
            keyVal+=9;
        }

        if(state.getEnemyHp().getRange() == HpRange.LOW) {
            keyVal+=10;
        } else if(state.getEnemyHp().getRange() == HpRange.MEDIUM_LOW) {
            keyVal+=11;
        } else if(state.getEnemyHp().getRange() == HpRange.MEDIUM_HIGH) {
            keyVal+=12;
        } else {
            keyVal+=13;
        }

        if(state.getClosestEnemy().getRange() == DistanceRange.CLOSE) {
            keyVal+=14;
        } else if(state.getClosestEnemy().getRange() == DistanceRange.MEDIUM_CLOSE) {
            keyVal+=15;
        } else if(state.getClosestEnemy().getRange() == DistanceRange.MEDIUM_FAR) {
            keyVal+=16;
        } else {
            keyVal+=17;
        }
        return keyVal;
    }

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
}