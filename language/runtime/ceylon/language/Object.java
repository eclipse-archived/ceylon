package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language.Void")
public abstract class Object extends Void {
    
    @Override
    public abstract boolean equals(@Name("that")
            java.lang.Object that);
    
    @Override
    public abstract int hashCode();

    @Override
    public java.lang.String toString() {
        return super.toString();
    }
}
