package src.main.java.ML.Range;

public enum DistanceRange {
    CLOSE(0.0, 100.0), MEDIUM_CLOSE(100.01, 150.0), MEDIUM_FAR(150.01, 200.0), FAR(200.01, Double.POSITIVE_INFINITY);
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
