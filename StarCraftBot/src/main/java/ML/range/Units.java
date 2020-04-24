package ml.range;

import java.io.Serializable;

/**
 * Represents a collection of units.
 */
public class Units implements Serializable {
    private static final long serialVersionUID = 1L;
    private UnitsRange range;
    private int value;

    /**
     * Initializes the units given a range.
     * @param range the range the number of units is between.
     */
    public Units(UnitsRange range) {
        this.range = range;
    }

    /**
     * Initializes the number of units given an int.
     * @param value the integer value of the range.
     */
    public Units(int value) {
        this.value = value;
        this.range = UnitsRange.get(value);
    }

    /**
     *
     * @return returns the range of the number of units
     */
    public UnitsRange getRange() {
        return range;
    }

    /**
     *
     * @param range the range to set.
     */
    public void setRange(UnitsRange range) {
        this.range = range;
    }

    /**
     *
     * @return returns the value of the number of units.
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
