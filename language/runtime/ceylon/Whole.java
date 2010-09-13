package ceylon;

public final class Whole
    extends Object
    implements Integral<Whole>, Invertable<Whole> {

    public ceylon.Whole plus(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole minus(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole times(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole divided(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole power(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole remainder(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole inverse() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Comparison compare(ceylon.Whole op) {
        throw new RuntimeException("not implemented");
    }

    @Extension
    public ceylon.String string() {
        throw new RuntimeException("not implemented");
    }

    // Conversions between numeric types

    public ceylon.Natural natural() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Integer integer() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Whole whole() {
        return this;
    }

    public ceylon.Float floatXXX() {
        throw new RuntimeException("not implemented");
    }

    @Extension
    public ceylon.Decimal decimal() {
        throw new RuntimeException("not implemented");
    }
}
