package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Method
public class see
{
    public static Nothing see(@Name("programElements") @Sequenced
            @TypeInfo("ceylon.language.Iterable<ceylon.language.Void>")
            final ceylon.language.Iterable<? extends java.lang.Object> value) {
        return null;
    }
    private see(){}
}
