package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;

@Ceylon
@Class(extendsType="ceylon.language.Void")
public abstract class Object extends Void {
    
    public java.lang.String toString() {
        return super.toString();
    }
    
    @Override
    public abstract boolean equals(@Name("that")
            java.lang.Object that);
    
    @Override
    public abstract int hashCode();
    
}
