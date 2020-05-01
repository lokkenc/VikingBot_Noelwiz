package ml.range;

import java.io.Serializable;

/**
 * Represents a distance between two points.
 */
public class Distance implements Serializable {
    private static final long serialVersionUID = 1L;
    private DistanceRange range;
    private int value;

    /**
     * Initializes the distance given a range.
     * @param range the range the distance is between.
     */
    public Distance(DistanceRange range) {
        this.range = range;
    }

    /**
     * Initializes the distance given an int.
     * @param value the integer value of the range.
     */
    public Distance(int value) {
        this.value = value;
        this.range = DistanceRange.get(value);
    }

    /**
     *
     * @return The range of the distance
     */
    public DistanceRange getRange() {
        return range;
    }

    /**
     *
     * @param range the range to set.
     */
    public void setRange(DistanceRange range) {
        this.range = range;
    }

    /**
     *
     * @return The integer value of the distance.
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value the value to set the distance to.
     */
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