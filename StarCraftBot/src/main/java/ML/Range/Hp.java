package ML.Range;

public class Hp implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private HpRange range;
    private int value;

    public Hp(HpRange range) {
        this.range = range;
    }

    public Hp(int value) {
        this.value = value;
        this.range = HpRange.get(value);
    }

    public HpRange getRange() {
        return range;
    }

    public void setRange(HpRange range) {
        this.range = range;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}