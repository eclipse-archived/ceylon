package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Exception extends RuntimeException {

    private ceylon.language.String description;
    public Exception(
            @TypeInfo("ceylon.language.String|ceylon.language.Nothing")
            @Name("description")
            ceylon.language.String description,
            @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
            @Name("cause")
            java.lang.Throwable cause
            
            ) {
        super(description.value, cause);
        this.description = description;
    }
    
    @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @TypeInfo("ceylon.language.String")
    public java.lang.String getMessage() {
        if (description != null
                && description.value != null) {
            return description.value;
        } else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }

    @TypeInfo("ceylon.language.String")
    public java.lang.String toString() {
        return  "Exception \"" + getMessage() +"\""; 
    }
}
