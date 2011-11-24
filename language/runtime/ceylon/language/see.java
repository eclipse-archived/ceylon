package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Method
public class see
{
    public static Nothing see(@Name("programElements") @Sequenced
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Void>")
            final ceylon.language.Iterable<? extends java.lang.Object> value) {
        return null;
    }
    private see(){}
}
