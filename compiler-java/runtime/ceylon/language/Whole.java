package ceylon.language;

public final class Whole
    extends Object
    implements Integral<Whole>, Invertable<Whole> {

    public ceylon.language.Whole plus(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole minus(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole times(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole divided(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole power(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole remainder(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole inverse() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Comparison compare(ceylon.language.Whole op) {
        throw new RuntimeException("not implemented");
    }

    @Extension
    public java.lang.String toString() {
        throw new RuntimeException("not implemented");
    }

    // Conversions between numeric types

    public ceylon.language.Natural natural() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Integer integer() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole whole() {
        return this;
    }

    public ceylon.language.Float toFloat() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole getPredecessor() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole getSuccessor() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean equals(java.lang.Object that) {
        // FIXME
        throw new RuntimeException("Not implemented");
    }
}
