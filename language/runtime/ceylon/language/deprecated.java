package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Method
public final class deprecated
{
    private deprecated(){}
    
    public static Nothing deprecated(
            @Defaulted
            @Name("reason") @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
            String reason) {
        return null;
    }

    @Ignore
    public static Nothing deprecated() {
        return deprecated($init$reason());
    }
    
    @Ignore
    public static String $init$reason() {
        return null;
    }
    
}
