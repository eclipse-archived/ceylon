package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class parseInteger
{
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public static Integer parseInteger(@Name("string") java.lang.String string) {
        return Integer.instance(java.lang.Long.parseLong(string));
    }
    private parseInteger(){}
}
