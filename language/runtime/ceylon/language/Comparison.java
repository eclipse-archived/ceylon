package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon
@Class(extendsType = "ceylon.language.IdentifiableObject")
@CaseTypes({"ceylon.language.equal", 
	        "ceylon.language.smaller", 
	        "ceylon.language.larger"})
public class Comparison {
    
    private final java.lang.String name;
	    
    public boolean largerThan() {
        return this == larger.getLarger();
    }

    public boolean smallerThan() {
        return this == smaller.getSmaller();
    }

    public boolean equal() {
        return this == equal.getEqual();
    }

    public boolean unequal() {
        return this != equal.getEqual();
    }

    public boolean asLargeAs() {
        return this != smaller.getSmaller();
    }

    public boolean asSmallAs() {
        return this != larger.getLarger();
    }

	Comparison(java.lang.String name) {
		this.name=name;
	}	
	
	@Override
	public java.lang.String toString() {
	    return name;
	}

}