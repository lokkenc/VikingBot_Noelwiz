
/*
 * Class: TerrainSector
 * Description: Represents a subsection of the visible map with collections of units in 8 directions.
 */
public class TerrainSector {
    public enum Direction {
        TOP_LEFT, TOP, TOP_RIGHT,
        LEFT, CENTER, RIGHT,
        BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
    }

    private Units units[][];

    public TerrainSector(Units unitDistances[][]) {
        this.units = unitDistances;
    }

    // Get the distance sums of all unit collections in every direction
    public double[][] getUnitDistanceSums() {
        double[][] unitDistances = new double[3][3];
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j< 3; j++) {
                unitDistances[i][j] = units[i][j].getUnitsSum();
            }
        }

        return unitDistances;
    }

    // Get the distance sums of all unit collections in every direction
    public double[][] getUnitDistanceAverages() {
        double[][] unitDistances = new double[3][3];
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j< 3; j++) {
                unitDistances[i][j] = units[i][j].getUnitsAverage();
            }
        }

        return unitDistances;
    }

    // TODO: Convert the rest of this class to use Units instead of double
    public double getMinDistance() {
        double min = Double.MAX_VALUE;
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
                if(units >= 0d && units[i][j] < min) {
                    min = units[i][j];
                }
            }
        }

        return min;
    }

    public double getMaxDistance() {
        double max = -1d;
        int i, j;

        for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
                if(units[i][j] > max) {
                    max = units[i][j];
                }
            }
        }

        return max;
    }
}