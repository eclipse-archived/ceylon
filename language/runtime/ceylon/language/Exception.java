package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ignore;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Exception extends RuntimeException {

	private static final long serialVersionUID = -1790691559137471641L;

	private java.lang.String description;
    
    public Exception(
            @TypeInfo("ceylon.language.String|ceylon.language.Nothing")
            @Name("description")
            java.lang.String description,
            @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
            @Name("cause")
            java.lang.Throwable cause
            
            ) {
        super(description==null ? null : description, cause);
        this.description = description;
    }
    
    @Ignore
    public Exception(java.lang.String description) {
    	this (description, null);
    }
    
    @Ignore
    public Exception() {
    	this (null, null);
    }
    
    @TypeInfo("ceylon.language.Exception|ceylon.language.Nothing")
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @TypeInfo("ceylon.language.String")
    public java.lang.String getMessage() {
        if (description != null
                && description != null) {
            return description;
        } 
        else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }

    @TypeInfo("ceylon.language.String")
    public java.lang.String toString() {
        return "Exception \"" + getMessage() +"\""; 
    }
    
    @Override
    public void printStackTrace() {
    	super.printStackTrace();
    }
}
