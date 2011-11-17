package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public final class Comparison extends Case {
	
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

	public Comparison(@Name("name") java.lang.String name) {
		super(name);
	}	

}