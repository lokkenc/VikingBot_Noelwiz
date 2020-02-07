

public class State {
    private double coolDown;
    private double friendlyHitPoints;
    private double enemyHitPoints;
    private TerrainSector friendlyDistanceSums;
    private TerrainSector friendlyDistanceMaxs;
    private TerrainSector enemyDistanceSums;
    private TerrainSector enemyDistanceMaxs;
    private TerrainSector terrainInfo;

    public State(double coolDown, double friendlyHitPoints, double enemyHitPoints, TerrainSector friendlyDistanceSums,
                 TerrainSector friendlyDistanceMaxs, TerrainSector enemyDistanceSums, TerrainSector enemyDistanceMaxs,
                 TerrainSector terrainInfo) {
        this.coolDown = coolDown;
        this.friendlyHitPoints = friendlyHitPoints;
        this.enemyHitPoints = enemyHitPoints;
        this.friendlyDistanceSums = friendlyDistanceSums;
        this.friendlyDistanceMaxs = friendlyDistanceMaxs;
        this.enemyDistanceSums = enemyDistanceSums;
        this.enemyDistanceMaxs = enemyDistanceMaxs;
        this.terrainInfo = terrainInfo;
    }

    public double getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(double coolDown) {
        this.coolDown = coolDown;
    }

    public double getFriendlyHitPoints() {
        return friendlyHitPoints;
    }

    public void setFriendlyHitPoints(double friendlyHitPoints) {
        this.friendlyHitPoints = friendlyHitPoints;
    }

    public double getEnemyHitPoints() {
        return enemyHitPoints;
    }

    public void setEnemyHitPoints(double enemyHitPoints) {
        this.enemyHitPoints = enemyHitPoints;
    }

    public TerrainSector getFriendlyDistanceSums() {
        return friendlyDistanceSums;
    }

    public void setFriendlyDistanceSums(TerrainSector friendlyDistanceSums) {
        this.friendlyDistanceSums = friendlyDistanceSums;
    }

    public TerrainSector getFriendlyDistanceMaxs() {
        return friendlyDistanceMaxs;
    }

    public void setFriendlyDistanceMaxs(TerrainSector friendlyDistanceMaxs) {
        this.friendlyDistanceMaxs = friendlyDistanceMaxs;
    }

    public TerrainSector getEnemyDistanceSums() {
        return enemyDistanceSums;
    }

    public void setEnemyDistanceSums(TerrainSector enemyDistanceSums) {
        this.enemyDistanceSums = enemyDistanceSums;
    }

    public TerrainSector getEnemyDistanceMaxs() {
        return enemyDistanceMaxs;
    }

    public void setEnemyDistanceMaxs(TerrainSector enemyDistanceMaxs) {
        this.enemyDistanceMaxs = enemyDistanceMaxs;
    }

    public TerrainSector getTerrainInfo() {
        return terrainInfo;
    }

    public void setTerrainInfo(TerrainSector terrainInfo) {
        this.terrainInfo = terrainInfo;
    }
}