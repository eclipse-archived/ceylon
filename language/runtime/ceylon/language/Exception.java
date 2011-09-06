package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Exception extends IdentifiableObject {

    public Exception(
            @TypeInfo("ceylon.language.String|ceylon.language.Nothing")
            ceylon.language.String message,
            @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
            java.lang.Throwable t) {
        // This isn't actually used at runtime, but is required by the type checker
    }
    
    public Exception getCause() {
        return null;
    }
    
    public ceylon.language.String getMessage() {
        return null;
    }
}
