package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class deprecated
{
    public static Nothing deprecated(
            @Defaulted
            @Name("reason") @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
            java.lang.String reason) {
        return null;
    }
    private deprecated(){}
    
    @Ignore
    public static java.lang.String $init$reason() {
        return null;
    }
    
}
