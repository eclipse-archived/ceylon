package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class tagged_
{
    public static Nothing tagged(@Name("tags") @Sequenced
            @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
            final ceylon.language.Iterable<? extends ceylon.language.String> tags) {
        return null;
    }
    @Ignore
    public static Nothing tagged() {
        return null;
    }
    private tagged_(){}
}
