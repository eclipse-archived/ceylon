package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

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
