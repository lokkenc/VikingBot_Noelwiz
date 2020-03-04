package Range;

public class Units {
    private UnitsRange range;
    private int value;

    public Units(UnitsRange range) {
        this.range = range;
    }

    public Units(int value) {
        this.value = value;
        this.range = UnitsRange.get(value);
    }

    public UnitsRange getRange() {
        return range;
    }

    public void setRange(UnitsRange range) {
        this.range = range;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
