package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class deprecated
{
    public static Nothing deprecated(@TypeInfo("ceylon.language.String|ceylon.language.Nothing")
                              @Name("reason") java.lang.String reason) {
        return null;
    }
    private deprecated(){}
}
