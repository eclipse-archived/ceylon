package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Throwable")
public class AssertionError extends java.lang.Error implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(AssertionError.class);

    private static final long serialVersionUID = -1790691559137471641L;

    @Ignore // JPA constructor
    protected AssertionError() {
        super();
        this.message = null;
    }
    
    @Ignore
    private final java.lang.String message;
    
    public AssertionError(
            @TypeInfo("ceylon.language::String")
            @Name("message")
            java.lang.String message) {
        super(message);
        this.message = message;
    }
        
    @Override
    @Ignore
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @Override
    @Ignore
    public java.lang.String getMessage() {
        return message;
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    @Override
    @Ignore
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

    //@Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
