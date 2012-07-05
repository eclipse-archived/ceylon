package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon(major = 2)
@Class(extendsType = "ceylon.language.IdentifiableObject")
@CaseTypes({"ceylon.language.true", "ceylon.language.false"})
public abstract class Boolean {

    @Ignore
    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();

}
