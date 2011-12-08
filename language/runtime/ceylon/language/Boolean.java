package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;

@Ceylon
@Class(extendsType = "ceylon.language.Case")
public abstract class Boolean extends Case {

    public Boolean(@Name("name") java.lang.String name) {
        super(name);
    }

    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();

}
