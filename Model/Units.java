
/*
 * Class: Units
 * Description: Represents a collection of units
 */
public class Units {
    private ArrayList<Unit> units;

    public Units(ArrayList<Unit> units) {
        this.units = units;

        // TODO: Sort units in ascending order
    }

    // Get the sum of all unit distances
    public double getUnitsSum() {

    }

    // Get the average distance of all units
    public double getUnitsAverage() {

    }

    // Get the closest unit
    public Unit getClosestUnit() {
        if(!units.isEmpty) {
            return units.get(0);
        }
    }

    // Get the farthest unit
    public Unit getFarthestUnit() {
        if(!units.isEmpty) {
            return units.get(units.size);
        }
    }
}