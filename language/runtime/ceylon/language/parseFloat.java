package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class parseFloat
{
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Float")
    public static Float parseFloat(@Name("string") java.lang.String string) {
        return Float.instance(java.lang.Double.parseDouble(string));
    }
    private parseFloat(){}
}
