package ceylon.language;

public final class Decimal
    extends Object
    implements Numeric<Decimal>, Invertable<Decimal> {

    public ceylon.language.Decimal plus(ceylon.language.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal minus(ceylon.language.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal times(ceylon.language.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal divided(ceylon.language.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal power(ceylon.language.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal inverse() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Comparison compare(ceylon.language.Decimal op) {
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
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Float floatXXX() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal decimal() {
        return this;
    }

    public ceylon.language.Decimal pred() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.language.Decimal succ() {
        throw new RuntimeException("not implemented");
    }
}
