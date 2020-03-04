package ML.States;

import ML.Range.Distance;
import ML.Range.Hp;
import ML.Range.Units;
import bwapi.Unit;
import ML.model.TerrainSector;

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
}