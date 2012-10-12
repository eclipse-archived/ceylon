package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon(major = 3) @Object
//TODO: extends Object directly!!!
@SatisfiedTypes("ceylon.language.Unit")
public class unit_ implements Unit {
    
    private final static unit_ value = new unit_();

    public static unit_ getUnit(){
        return value;
    }

    @Override
    public java.lang.String toString() {
        return "()";
    }
    
    @Override
    public boolean equals(java.lang.Object obj) {
    	return obj instanceof Unit;
    }
    
    @Override
    public int hashCode() {
    	return 0;
    }
}
