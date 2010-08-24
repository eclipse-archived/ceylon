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

    public ceylon.String asString() {
        java.lang.String result;
        switch (this) {
        case LARGER:
            result = "LARGER";
            break;
        case SMALLER:
            result = "SMALLER";
            break;
        case EQUAL:
            result = "EQUAL";
            break;
        default:
            throw new RuntimeException();
        }
        return ceylon.String.instance(result);
    }
}
