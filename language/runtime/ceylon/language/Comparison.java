package ceylon.language;

public class Comparison extends Case {
	
    public static final Comparison LARGER = new Comparison("larger");
    public static final Comparison SMALLER = new Comparison("smaller");
    public static final Comparison EQUAL = new Comparison("equal");
    
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
        return super.toString();
    }

	public Comparison(java.lang.String caseName) {
		super(caseName);
	}	

}