package Range;

public enum DistanceRange {
    CLOSE(0.0, 0.25), MEDIUM_CLOSE(0.26, 0.75), MEDIUM_FAR(0.76, 100.0), FAR(100.0, 120.0);
    double min;
    double max;

    DistanceRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static DistanceRange get(double value) {
        for (DistanceRange dist : values()) {
            if (dist.in(value))
                return dist;
        }
        return CLOSE;
    }

    public boolean in(double v) {
        return v >= min && v <= max;
    }
}
