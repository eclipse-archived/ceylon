package ceylon;

public enum Comparison {
    LARGER,
    SMALLER,
    EQUAL;

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
}
