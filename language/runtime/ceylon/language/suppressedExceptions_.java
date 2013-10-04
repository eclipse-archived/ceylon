package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 5)
@Method
public final class suppressedExceptions_ {
    
    private suppressedExceptions_() {}
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Exception>")
    public static ceylon.language.Sequential<? extends java.lang.Throwable> suppressedExceptions(
            @Name("exception")
            @TypeInfo("ceylon.language::Exception")
            final java.lang.Throwable exception) {
        Throwable[] sup = exception.getSuppressed();
        if (sup.length > 0) {
            return ArraySequence.backedBy$hidden(ceylon.language.Exception.$TypeDescriptor, sup, 0, sup.length);
        } else {
            return (ceylon.language.Sequential)empty_.get_();
        }
    }

}
