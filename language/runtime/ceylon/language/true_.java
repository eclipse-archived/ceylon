package ceylon.language;

import java.io.ObjectStreamException;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8) 
@Object
@Class(extendsType = "ceylon.language::Boolean")
@ValueType
public final class true_ extends Boolean {

    private static final long serialVersionUID = -6256274272803665251L;
    
    java.lang.Object readResolve() 
            throws ObjectStreamException {
        return value;
    }
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ 
            = TypeDescriptor.klass(true_.class);

    private final static true_ value = new true_();

    public static true_ get_(){
        return value;
    }

    @Override
    @Ignore
    public boolean booleanValue() {
        return true;
    }
    
    @Override
    @Transient
    public java.lang.String toString() {
        return "true";
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$(){
        return $TypeDescriptor$;
    }
}
