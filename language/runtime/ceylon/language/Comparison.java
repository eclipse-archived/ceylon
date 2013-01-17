package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon(major = 4)
@Class
@CaseTypes({"ceylon.language::equal", 
	        "ceylon.language::smaller", 
	        "ceylon.language::larger"})
public class Comparison {
    
    private final java.lang.String name;

    @Ignore
    public boolean largerThan() {
        return this == larger_.getLarger$();
    }

    @Ignore
    public boolean smallerThan() {
        return this == smaller_.getSmaller$();
    }

    @Ignore
    public boolean equal() {
        return this == equal_.getEqual$();
    }

    @Ignore
    public boolean unequal() {
        return this != equal_.getEqual$();
    }

    @Ignore
    public boolean asLargeAs() {
        return this != smaller_.getSmaller$();
    }

    @Ignore
    public boolean asSmallAs() {
        return this != larger_.getLarger$();
    }

	Comparison(java.lang.String name) {
		this.name=name;
	}	
	
	@Override
	public java.lang.String toString() {
	    return name;
	}

}