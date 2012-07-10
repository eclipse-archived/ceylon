package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

@Ceylon(major = 2)
@Class(extendsType = "ceylon.language.IdentifiableObject")
@CaseTypes({"ceylon.language.true", "ceylon.language.false"})
@ValueType
public abstract class Boolean {

    @Ignore
    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();
    
    @Ignore
    public static java.lang.String toString(boolean value) {
        return (value) ? $true.getTrue().toString() : $false.getFalse().toString();
    }

}
