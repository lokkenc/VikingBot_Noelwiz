package ML.model;

import bwapi.Unit;

/*
 * Class: ML.model.TerrainSector
 * Description: Represents a subsection of the visible map with collections of units in 8 directions.
 */
public class TerrainSector {

    private UnitGroup[][] units;

    public TerrainSector(UnitGroup[][] units) {
        this.units = units;
    }

    // Get the distance sums of all unit collections in every direction
    public double[][] getUnitDistanceSumsFromUnit(Unit source) {
        double[][] unitDistances = new double[3][3];
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j< 3; j++) {
                unitDistances[i][j] = units[i][j].getTotalDistanceFromUnit(source);
            }
        }

        return unitDistances;
    }

    // Get the distance sums of all unit collections in every direction
    public double[][] getUnitDistanceAverages(Unit source) {
        double[][] unitDistances = new double[3][3];
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
                unitDistances[i][j] = units[i][j].getAverageDistanceFromUnit(source);
            }
        }

        return unitDistances;
    }

    public double getMinDistanceFromUnit(Unit source) {
        double min = Double.MAX_VALUE;
        Unit closestUnit;
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
               closestUnit = units[i][j].getClosestUnitToUnit(source);

               if(closestUnit.getDistance(source) < min) {
                   min = closestUnit.getDistance(source);
               }
            }
        }

        return min;
    }

    public double getMaxDistanceFromUnit(Unit source) {
        double max = -1d;
        Unit farthestUnit;
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
                farthestUnit = units[i][j].getFarthestUnitFromUnit(source);

                if(farthestUnit.getDistance(source) > max) {
                    max = farthestUnit.getDistance(source);
                }
            }
        }

        return max;
    }
}