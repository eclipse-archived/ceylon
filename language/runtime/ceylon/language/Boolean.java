package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon
@Class(extendsType = "ceylon.language.IdentifiableObject")
@CaseTypes({"ceylon.language.truth", "ceylon.language.falsity"})
public abstract class Boolean {

    @Ignore
    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();

}
