package ceylon;

public final class Decimal
    extends Object
    implements Numeric<Decimal>, Invertable<Decimal> {

    public ceylon.Decimal plus(ceylon.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal minus(ceylon.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal times(ceylon.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal divided(ceylon.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal power(ceylon.Decimal op) {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal inverse() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Comparison compare(ceylon.Decimal op) {
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
        throw new RuntimeException("not implemented");
    }

    public ceylon.Float floatXXX() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Decimal decimal() {
        return this;
    }
}
