package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class(extendsType="ceylon.language::Throwable")
public class Error extends ceylon.language.Throwable /*implements ReifiedType*/ {
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Error.class);

    @Ignore
    public Error() {
        super();
    }

    public Error(
            @TypeInfo("ceylon.language::String|ceylon.language::Null")
            @Name("description")
            @Defaulted
            java.lang.String description,
            @TypeInfo("ceylon.language::Throwable|ceylon.language::Null")
            @Name("cause")
            @Defaulted
            java.lang.Throwable cause) {
        super(description, cause);
    }

    @Ignore
    public Error(@TypeInfo("ceylon.language::String|ceylon.language::Null")
            @Name("description")
            @Defaulted
            java.lang.String description) {
        super(description);
    }
    @Ignore
    public static java.lang.String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(String description){
        return null;
    }
    
}
