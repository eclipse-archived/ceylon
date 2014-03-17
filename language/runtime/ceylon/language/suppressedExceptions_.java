package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Method
public final class suppressedExceptions_ {
    
    private suppressedExceptions_() {}
    
    // TODO Really the return is is <Throwable|Object>[] because this could include
    // a java subclass of Throwable (whose supertype for Ceylon purposes must be Object)
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Throwable>")
    public static ceylon.language.Sequential<? extends java.lang.Throwable> suppressedExceptions(
            @Name("exception")
            @TypeInfo("ceylon.language::Exception")
            final java.lang.Throwable exception) {
        java.lang.Throwable[] sup = exception.getSuppressed();
        if (sup.length > 0) {
            return ArraySequence.backedBy$hidden(TypeDescriptor.klass(java.lang.Throwable.class), sup, 0, sup.length);
        } else {
            return (ceylon.language.Sequential)empty_.get_();
        }
    }

}
