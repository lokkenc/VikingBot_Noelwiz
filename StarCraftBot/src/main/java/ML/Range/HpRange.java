package ML.Range;

public enum HpRange {
    LOW(0.0, 25.0), MEDIUM_LOW(25.01, 50.0), MEDIUM_HIGH(50.01, 75.0), HIGH(75.01, 100.0);
    double min;
    double max;

    HpRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static HpRange get(double value) {
        for (HpRange hp : values()) {
            if (hp.in(value))
                return hp;
        }
        return LOW;
    }

    public boolean in(double v) {
        return v >= min && v <= max;
    }
}
