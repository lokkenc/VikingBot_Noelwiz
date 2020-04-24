package ML.Range;

/**
 * Represents a range of possible hp values and categorizes them into a relative enum.
 */
public enum HpRange {
    LOW(0.0, 25.0), MEDIUM_LOW(25.01, 50.0), MEDIUM_HIGH(50.01, 75.0), HIGH(75.01, 100.0);
    double min;
    double max;

    /**
     * Initializes the range given a min value and a max value.
     * @param min the smallest possible hp.
     * @param max the largest possible hp.
     */
    HpRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Takes a value and returns the respective range.
     * @param value the real value to be converted into a range.
     * @return returns the range that value is in.
     */
    public static HpRange get(double value) {
        for (HpRange hp : values()) {
            if (hp.in(value))
                return hp;
        }
        return LOW;
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
