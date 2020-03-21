package ML.Range;

import java.io.Serializable;

public class Distance implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return /*value == distance.value &&*/
                range == distance.range;
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
        return "Distance{" +
                "range=" + range +
                ", value=" + value +
                '}';
    }
}