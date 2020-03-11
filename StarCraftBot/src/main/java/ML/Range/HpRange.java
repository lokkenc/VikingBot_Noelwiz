package src.main.java.ML.Range;

public enum HpRange {
    LOW(0.0, 0.25), MEDIUM_LOW(0.26, 0.50), MEDIUM_HIGH(0.51, 0.75), HIGH(0.75, 100.0);
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

    public boolean onEqual(HpRange range2) {
        if(this == range2) {
            return true;
        }

        return false;
    }
}
