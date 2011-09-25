package ceylon.language;

public class Comparison extends Object {
    public static final Comparison LARGER = new Comparison();
    public static final Comparison SMALLER = new Comparison();
    public static final Comparison EQUAL = new Comparison();

    public boolean larger() {
        return this == LARGER;
    }

    public boolean smaller() {
        return this == SMALLER;
    }

    public boolean equal() {
        return this == EQUAL;
    }

    public boolean unequal() {
        return this != EQUAL;
    }

    public boolean largeAs() {
        return this != SMALLER;
    }

    public boolean smallAs() {
        return this != LARGER;
    }

    public java.lang.String toString() {
        java.lang.String result;
        if (this == LARGER)
            result = "LARGER";
        else if (this == SMALLER)
            result = "SMALLER";
        else
            result = "EQUAL";

        return result;
    }
}
