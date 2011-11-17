package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public abstract class Boolean extends Case {

    public Boolean(@Name("name") java.lang.String name) {
        super(name);
    }

    public static Boolean instance(boolean b) {
        return b ? $true.getTrue() : $false.getFalse();
    }

    abstract public boolean booleanValue();

}
