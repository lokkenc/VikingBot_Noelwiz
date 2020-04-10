package ML.Range;

import java.io.Serializable;

public class Hp implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hp hp = (Hp) o;
        return /*value == distance.value &&*/
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