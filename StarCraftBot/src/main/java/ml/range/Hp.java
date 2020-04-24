package ML.Range;

import java.io.Serializable;

/**
 * Represents the total hp from a group of units.
 */
public class Hp implements Serializable {
    private static final long serialVersionUID = 1L;
    private HpRange range;
    private int value;

    /**
     * Initializes the hp given a range.
     * @param range the range the hp is between.
     */
    public Hp(HpRange range) {
        this.range = range;
    }

    /**
     * Initializes the hp given an int.
     * @param value the integer value of the range.
     */
    public Hp(int value) {
        this.value = value;
        this.range = HpRange.get(value);
    }

    /**
     *
     * @return returns the range of the hp
     */
    public HpRange getRange() {
        return range;
    }

    /**
     *
     * @param range the range to set.
     */
    public void setRange(HpRange range) {
        this.range = range;
    }

    /**
     *
     * @return returns the integer value of the hp.
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value the value to set the hp to.
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hp hp = (Hp) o;
        return /*value == hp.value &&*/
                range == hp.range;
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
        return "Hp{" +
                "range=" + range +
                ", value=" + value +
                '}';
    }
}