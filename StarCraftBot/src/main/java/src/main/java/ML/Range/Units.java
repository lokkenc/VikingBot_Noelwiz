package src.main.java.ML.Range;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Units units = (Units) o;
        return /*value == distance.value &&*/
                range == units.range;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((range == null) ? 0 : range.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Units{" +
                "range=" + range +
                ", value=" + value +
                '}';
    }
}
