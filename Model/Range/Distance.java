package Range;

public class Distance {
    private DistanceRange range;
    private int value;

    public Distance(DistanceRange range) {
        this.range = range;
    }

    public Distance(int value) {
        this.value = value;
        this.range = DistanceRange.get(value);
    }

    public DistanceRange getRange() {
        return range;
    }

    public void setRange(DistanceRange range) {
        this.range = range;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}