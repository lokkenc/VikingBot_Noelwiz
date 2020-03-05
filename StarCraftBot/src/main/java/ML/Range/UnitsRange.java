package ML.Range;

public enum UnitsRange {
    SMALL(0, 5), MEDIUM(6, 25), LARGE(26, 200);
    double min;
    double max;

    UnitsRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static UnitsRange get(double value) {
        for (UnitsRange unit : values()) {
            if (unit.in(value))
                return unit;
        }
        return SMALL;
    }

    public boolean in(double v) {
        return v >= min && v <= max;
    }

    public boolean onEqual(UnitsRange range2) {
        if(this == range2) {
            return true;
        }

        return false;
    }
}