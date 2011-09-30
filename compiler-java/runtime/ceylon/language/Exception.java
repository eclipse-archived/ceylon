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
    
    public java.lang.Exception getCause() {
        return (java.lang.Exception)super.getCause();
    }
    
    public java.lang.String getMessage() {
        if (description != null) {
            return description.value;
        } else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }
    
    public java.lang.String toString() {
        return /*TODO IdentifiableObject.toString() + */ " \"" + getMessage() +"\""; 
    }
}
