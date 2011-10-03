package ceylon.language;

public class Comparable$impl {
    public static <Other extends Comparable<Other>> boolean largerThan(Comparable<Other> $this, Other other) {
        return $this.compare(other)==Comparison.LARGER;
    }

    public static <Other extends Comparable<Other>> boolean smallerThan(Comparable<Other> $this, Other other) {
        return $this.compare(other)==Comparison.SMALLER;
    }

    public static <Other extends Comparable<Other>> boolean asLargeAs(Comparable<Other> $this, Other other) {
        return $this.compare(other)!=Comparison.SMALLER;
    }

    public static <Other extends Comparable<Other>> boolean asSmallAs(Comparable<Other> $this, Other other) {
        return $this.compare(other)!=Comparison.LARGER;
    }

}
