package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class tagged
{
    public static Nothing tagged(@Name("tags") @Sequenced
            @TypeInfo("ceylon.language.Iterable<ceylon.language.String>")
            final ceylon.language.Iterable<? extends ceylon.language.String> tags) {
        return null;
    }
    private tagged(){}
}
