package Project2;

import java.util.Objects;

public class Tuple implements Comparable<Tuple> {
    public final Integer x;
    public final Integer y;
    public Tuple(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public int compareTo(Tuple tuple) {
        int first = this.x.compareTo(tuple.x);
        return first == 0 ? this.y.compareTo(tuple.y) : first;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        return (Objects.equals(x, tuple.x) && Objects.equals(y, tuple.y));

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
