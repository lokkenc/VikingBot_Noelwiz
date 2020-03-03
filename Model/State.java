public class State {
    private double coolDown;
    private Units friendlyUnits;
    private Units enemyUnits;
    private double friendlyHitPoints;
    private double enemyHitPoints;
    private TerrainSector friendlyDistanceSums;
    private TerrainSector friendlyDistanceMaxes;
    private TerrainSector enemyDistanceSums;
    private TerrainSector enemyDistanceMaxes;
    private TerrainSector terrainInfo;

    public State(double coolDown, Units friendlyUnits, Units enemyUnits, TerrainSector friendlyDistanceSums,
                 TerrainSector friendlyDistanceMaxes, TerrainSector enemyDistanceSums, TerrainSector enemyDistanceMaxes,
                 TerrainSector terrainInfo) {
        this.coolDown = coolDown;
        this.friendlyUnits = friendlyUnits;
        this.enemyUnits = enemyUnits;
        this.friendlyDistanceSums = friendlyDistanceSums;
        this.friendlyDistanceMaxes = friendlyDistanceMaxes;
        this.enemyDistanceSums = enemyDistanceSums;
        this.enemyDistanceMaxes = enemyDistanceMaxes;
        this.terrainInfo = terrainInfo;

        this.friendlyHitPoints = this.friendlyUnits.getTotalHp();
        this.enemyHitPoints = this.enemyUnits.getTotalHp();
    }

    public double getCoolDown() {
        return coolDown;
    }

    public Units getFriendlyUnits() {
        return this.friendlyUnits;
    }

    public Units getEnemyUnits() {
        return this.enemyUnits;
    }

    public double getFriendlyHitPoints() {
        return friendlyHitPoints;
    }

    public double getEnemyHitPoints() {
        return enemyHitPoints;
    }

    public TerrainSector getFriendlyDistanceSums() {
        return friendlyDistanceSums;
    }

    public TerrainSector getFriendlyDistanceMaxes() {
        return friendlyDistanceMaxes;
    }

    public TerrainSector getEnemyDistanceSums() {
        return enemyDistanceSums;
    }

    public TerrainSector getEnemyDistanceMaxes() {
        return enemyDistanceMaxes;
    }

    public TerrainSector getTerrainInfo() {
        return terrainInfo;
    }
}