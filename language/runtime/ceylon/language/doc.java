package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;

@Ceylon(major = 2)
@Method
public final class doc
{
    public static Nothing doc(@Name("description") java.lang.String description) {
        return null;
    }
    private doc(){}
}
