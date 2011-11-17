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
}
