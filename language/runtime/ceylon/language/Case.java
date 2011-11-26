package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public abstract class Case {
    private final java.lang.String string;
    
    public Case(@Name("caseName") java.lang.String caseName){
        this.string = caseName;
    }
    
    public java.lang.String toString(){
        return string;
    }
    
    @Override
    public final boolean equals(java.lang.Object that) {
    	return super.equals(that);
    }
    
    @Override
    public final int hashCode() {
    	return super.hashCode();
    }
}
