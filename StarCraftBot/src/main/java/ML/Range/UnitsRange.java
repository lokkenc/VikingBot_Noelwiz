package ML.Range;

/**
 * Represents a range of possible nearby units and categorizes them into a relative enum.
 */
public enum UnitsRange {
    SMALL(0, 5), MEDIUM(6, 25), LARGE(26, 200);
    double min;
    double max;

    /**
     * Initializes the range given a min value and a max value.
     * @param min the smallest possible number of units.
     * @param max the largest possible number of units.
     */
    UnitsRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Takes a value and returns the respective range.
     * @param value the real value to be converted into a range.
     * @return returns the range that value is in.
     */
    public static UnitsRange get(double value) {
        for (UnitsRange unit : values()) {
            if (unit.in(value))
                return unit;
        }
        return SMALL;
    }

    /**
     * Checks if a real value is in the range.
     * @param v real value to check.
     * @return returns whether the value is in the range or not.
     */
    public boolean in(double v) {
        return v >= min && v <= max;
    }
}