package ceylon;

public class Comparison extends Object {
    public static final Comparison LARGER = new Comparison();
    public static final Comparison SMALLER = new Comparison();
    public static final Comparison EQUAL = new Comparison();

    public ceylon.Boolean larger() {
        return ceylon.Boolean.instance(this == LARGER);
    }

    public ceylon.Boolean smaller() {
        return ceylon.Boolean.instance(this == SMALLER);
    }

    public ceylon.Boolean equal() {
        return ceylon.Boolean.instance(this == EQUAL);
    }

    public ceylon.Boolean unequal() {
        return ceylon.Boolean.instance(this != EQUAL);
    }

    public ceylon.Boolean largeAs() {
        return ceylon.Boolean.instance(this != SMALLER);
    }

    public ceylon.Boolean smallAs() {
        return ceylon.Boolean.instance(this != LARGER);
    }

    public ceylon.String string() {
        java.lang.String result;
        if (this == LARGER)
            result = "LARGER";
        else if (this == SMALLER)
            result = "SMALLER";
        else
            result = "EQUAL";

        return ceylon.String.instance(result);
    }
}
