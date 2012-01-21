package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;

@Ceylon
@Class(extendsType = "ceylon.language.Case")
@CaseTypes({"ceylon.language.true", "ceylon.language.false"})
public abstract class Boolean extends Case {

    public Boolean(@Name("name") java.lang.String name) {
        super(name);
    }

    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();

}
