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
    public ceylon.language.String string() {
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

    public ceylon.language.Float floatXXX() {
        throw new RuntimeException("not implemented");
    }

    @Extension
    public ceylon.language.Decimal decimal() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole getPredecessor() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Whole getSuccessor() {
        throw new RuntimeException("not implemented");
    }
}
