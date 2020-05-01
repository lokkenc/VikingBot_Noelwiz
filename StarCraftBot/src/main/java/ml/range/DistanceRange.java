package ml.range;

/**
 * Represents a range of possible distances and categorizes them into a relative enum.
 */
public enum DistanceRange {
    CLOSE(0.0, 100.0), MEDIUM_CLOSE(100.01, 150.0), MEDIUM_FAR(150.01, 200.0), FAR(200.01, Double.POSITIVE_INFINITY);
    double min;
    double max;

    /**
     * Initializes the range given a min value and a max value.
     * @param min the smallest possible distance.
     * @param max the largest possible distance.
     */
    DistanceRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Takes a value and returns the respective range.
     * @param value the real value to be converted into a range.
     * @return The range that value is in.
     */
    public static DistanceRange get(double value) {
        for (DistanceRange dist : values()) {
            if (dist.in(value))
                return dist;
        }
        return CLOSE;
    }

    /**
     * Checks if a real value is in the range.
     * @param v real value to check.
     * @return Whether the value is in the range or not.
     */
    public boolean in(double v) {
        return v >= min && v <= max;
    }
}
