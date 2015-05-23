package train;

/**
 * Created by gongyu on 2015/4/25.
 * Define train.Pair data structure.
 */
public class Pair<T> {
    public T val1;
    public T val2;

    public Pair(T val1, T val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public int hashCode() {
        return val1.hashCode() + val2.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Pair)) return false;
        Pair<T> pair = (Pair<T>) obj;
        return (this.val1.equals(pair.val1) && this.val2.equals(pair.val2));
    }

    @Override
    public String toString() {
        return "(" + val1 + "," + val2 + ")";
    }
}
