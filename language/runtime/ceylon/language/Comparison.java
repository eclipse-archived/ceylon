package ceylon.language;

public class Comparison extends Object {
    public static final Comparison LARGER = new Comparison();
    public static final Comparison SMALLER = new Comparison();
    public static final Comparison EQUAL = new Comparison();

    public ceylon.language.Boolean larger() {
        return ceylon.language.Boolean.instance(this == LARGER);
    }

    public ceylon.language.Boolean smaller() {
        return ceylon.language.Boolean.instance(this == SMALLER);
    }

    public ceylon.language.Boolean equal() {
        return ceylon.language.Boolean.instance(this == EQUAL);
    }

    public ceylon.language.Boolean unequal() {
        return ceylon.language.Boolean.instance(this != EQUAL);
    }

    public ceylon.language.Boolean largeAs() {
        return ceylon.language.Boolean.instance(this != SMALLER);
    }

    public ceylon.language.Boolean smallAs() {
        return ceylon.language.Boolean.instance(this != LARGER);
    }

    @Extension
    public ceylon.language.String string() {
        java.lang.String result;
        if (this == LARGER)
            result = "LARGER";
        else if (this == SMALLER)
            result = "SMALLER";
        else
            result = "EQUAL";

        return ceylon.language.String.instance(result);
    }
}
