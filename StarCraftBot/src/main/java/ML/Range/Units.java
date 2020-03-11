package src.main.java.ML.Range;

import java.io.Serializable;

public class Units implements Serializable {
    private static final long serialVersionUID = 1L;
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
