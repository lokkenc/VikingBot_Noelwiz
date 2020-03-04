package States;

import Range.Distance;
import Range.Hp;
import Range.Units;
import model.TerrainSector;

public class State {
    private boolean coolDown;
    private Distance closestEnemy;
    private Units numberOfEnemies;
    private Hp enemyHp;
    private Hp friendlyHp;
    private TerrainSector unitDirections;

    public State(boolean coolDown, Distance closestEnemy, Units numberOfEnemies, Hp enemyHp, Hp friendlyHp, TerrainSector unitDirections) {
        this.coolDown = coolDown;
        this.closestEnemy = closestEnemy;
        this.numberOfEnemies = numberOfEnemies;
        this.enemyHp = enemyHp;
        this.friendlyHp = friendlyHp;
        this.unitDirections = unitDirections;
    }

    public State(boolean coolDown, int closestEnemy, int numberOfEnemies, int enemyHp, int friendlyHp, TerrainSector unitDirections) {
        this.coolDown = coolDown;
        this.closestEnemy = new Distance(closestEnemy);
        this.numberOfEnemies = new Units(numberOfEnemies);
        this.enemyHp = new Hp(enemyHp);
        this.friendlyHp = new Hp(friendlyHp);
        this.unitDirections = unitDirections;
    }

    public boolean isCoolDown() {
        return coolDown;
    }

    public Distance getClosestEnemy() {
        return closestEnemy;
    }

    public Units getNumberOfEnemies() {
        return numberOfEnemies;
    }

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