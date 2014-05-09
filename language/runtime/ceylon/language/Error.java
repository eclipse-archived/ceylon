package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType = "ceylon.language::Throwable")
public class Error extends java.lang.Error implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Exception.class);

	private static final long serialVersionUID = -1790691559137471641L;

	private String description;
    
    public Error(
            @TypeInfo("ceylon.language::Null|ceylon.language::String")
            @Name("description")
            @Defaulted
            String description,
            @TypeInfo("ceylon.language::Null|ceylon.language::Throwable")
            @Name("cause")
            @Defaulted
            java.lang.Throwable cause) {
        super(description==null ? null : description.toString(), cause);
        this.description = description;
    }
    
    @Ignore
    public Error(String description) {
        this(description, $default$cause(description));
    }
    
    @Ignore
    public Error() {
        this($default$description());
    }
        
    @Ignore
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @Override
    @Ignore
    public java.lang.String getMessage() {
        if (description != null) {
            return description.toString();
        } 
        else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    @Override
    public void printStackTrace() {
    	super.printStackTrace();
    }

    @Ignore
    public static String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(String description){
        return null;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
