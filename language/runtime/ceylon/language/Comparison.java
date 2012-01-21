package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon
@Class(extendsType = "ceylon.language.IdentifiableObject")
public final class Comparison {
    
    private final java.lang.String name;
	
    public static final Comparison LARGER = new Comparison("larger");
    public static final Comparison SMALLER = new Comparison("smaller");
    public static final Comparison EQUAL = new Comparison("equal");
    
    public boolean largerThan() {
        return this == LARGER;
    }

    public boolean smallerThan() {
        return this == SMALLER;
    }

    public boolean equal() {
        return this == EQUAL;
    }

    public boolean unequal() {
        return this != EQUAL;
    }

    public boolean asLargeAs() {
        return this != SMALLER;
    }

    public boolean asSmallAs() {
        return this != LARGER;
    }

	private Comparison(java.lang.String name) {
		this.name=name;
	}	
	
	@Override
	public java.lang.String toString() {
	    return name;
	}

}