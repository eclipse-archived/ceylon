package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;

@Ceylon
@Class(extendsType="ceylon.language.Void")
public abstract class Object extends Void {

    public java.lang.String toString() {
        return super.toString();
    }
    
}
