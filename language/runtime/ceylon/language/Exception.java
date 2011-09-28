package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Exception extends RuntimeException {

    public Exception(
            @TypeInfo("ceylon.language.String|ceylon.language.Nothing")
            @Name("message")
            ceylon.language.String message,
            @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
            @Name("cause")
            java.lang.Throwable cause) {
        // This isn't actually used at runtime, but is required by the type checker
    }
    
    public Exception getCause() {
        return null;
    }
    
    public ceylon.language.String getMessage() {
        return null;
    }
}
