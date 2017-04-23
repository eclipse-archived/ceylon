package ceylon.language;

import com.redhat.ceylon.common.NonNull;
import com.redhat.ceylon.common.Nullable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Basic")
@SharedAnnotation$annotation$
@AbstractAnnotation$annotation$
@SealedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public abstract class Throwable extends java.lang.Object 
        implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Throwable.class);

    @Ignore
    private final java.lang.String description;
    
    public Throwable(
            @TypeInfo("ceylon.language::String?")
            @Name("description")
            @Defaulted
            @Nullable
            String description,
            @TypeInfo("ceylon.language::Throwable?")
            @Name("cause")
            @Defaulted
            @Nullable
            java.lang.Throwable cause) {
        //super(description==null ? null : description.toString(), cause);
        this.description = description.value;
    }
    
    @Ignore
    public Throwable(String description) {
        this(description, $default$cause(description));
    }
    
    @Ignore
    public Throwable() {
        this($default$description());
    }
        
    @TypeInfo("ceylon.language::Throwable?")
    @Nullable
    public final java.lang.Throwable getCause() {
        return null;//super.getCause();
    }
    
    @TypeInfo("ceylon.language::String")
    @Transient
    @NonNull
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
    @Transient
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    //@Override
    public final void printStackTrace() {
    	//super.printStackTrace();
    }

    @Ignore
    public static String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(String description){
        return null;
    }
    
    @TypeInfo("ceylon.language::Throwable[]")
    @NonNull
    public final Sequential<? extends java.lang.Throwable> getSuppressed() {
        return null;
    }
    
    public final void addSuppressed(
            @Name("suppressed")
            @TypeInfo("ceylon.language::Throwable")
            @NonNull
            java.lang.Throwable suppressed) {}
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
