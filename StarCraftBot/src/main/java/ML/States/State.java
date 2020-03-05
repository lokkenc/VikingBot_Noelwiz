package ML.States;

import ML.Range.*;
import bwapi.Unit;
import ML.model.TerrainSector;

import static ML.Range.UnitsRange.*;

public class State {
    private boolean onCoolDown;
    private Distance closestEnemy;
    private Units numberOfEnemies;
    private Unit Unit;
    private Hp enemyHp;
    private Hp friendlyHp;
    private TerrainSector unitDirections;

    public State(boolean onCoolDown, Distance closestEnemy, Units numberOfEnemies, Hp enemyHp, Hp friendlyHp) {
        this.onCoolDown = onCoolDown;
        this.closestEnemy = closestEnemy;
        this.numberOfEnemies = numberOfEnemies;
        this.Unit = null;
        this.enemyHp = enemyHp;
        this.friendlyHp = friendlyHp;
        this.unitDirections = null;
    }

    public void setUnit(Unit unit) {
        this.Unit = unit;
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

    public Unit getUnit(){return Unit; }

    public Hp getEnemyHp() {
        return enemyHp;
    }

    public Hp getFriendlyHp() {
        return friendlyHp;
    }

    public TerrainSector getUnitDirections() {
        return unitDirections;
    }

    public int getKeyVal(State state) {
        int keyVal = 0;

        if(state.isOnCoolDown()) {
            keyVal+=1;
        } else {
            keyVal+=2;
        }

        if(state.getNumberOfEnemies().getRange() == SMALL) {
            keyVal+=1;
        } else if(state.getNumberOfEnemies().getRange() == MEDIUM) {
            keyVal+=2;
        } else if(state.getNumberOfEnemies().getRange() == LARGE) {
            keyVal+=3;
        }

        if(state.getFriendlyHp().getRange() == HpRange.LOW) {
            keyVal+=1;
        } else if(state.getFriendlyHp().getRange() == HpRange.MEDIUM_LOW) {
            keyVal+=2;
        } else if(state.getFriendlyHp().getRange() == HpRange.MEDIUM_HIGH) {
            keyVal+=3;
        } else {
            keyVal+=4;
        }

        if(state.getEnemyHp().getRange() == HpRange.LOW) {
            keyVal+=1;
        } else if(state.getEnemyHp().getRange() == HpRange.MEDIUM_LOW) {
            keyVal+=2;
        } else if(state.getEnemyHp().getRange() == HpRange.MEDIUM_HIGH) {
            keyVal+=3;
        } else {
            keyVal+=4;
        }

        if(state.getClosestEnemy().getRange() == DistanceRange.CLOSE) {
            keyVal+=1;
        } else if(state.getClosestEnemy().getRange() == DistanceRange.MEDIUM_CLOSE) {
            keyVal+=2;
        } else if(state.getClosestEnemy().getRange() == DistanceRange.MEDIUM_FAR) {
            keyVal+=3;
        } else {
            keyVal+=4;
        }
        return keyVal;
    }
}