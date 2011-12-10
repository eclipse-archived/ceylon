package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;

@Ceylon
@Class(extendsType = "ceylon.language.Case")
public final class Comparison extends Case {
	
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

	private Comparison(@Name("name") java.lang.String name) {
		super(name);
	}	

}