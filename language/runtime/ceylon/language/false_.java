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
public final class false_ extends Boolean {
    
    private static final long serialVersionUID = -5577975222336608937L;

    java.lang.Object readResolve() 
            throws ObjectStreamException {
        return value;
    }
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ 
            = TypeDescriptor.klass(false_.class);

    private final static false_ value = new false_();

    public static false_ get_(){
        return value;
    }

    @Override
    @Ignore
    public boolean booleanValue() {
        return false;
    }
    
    @Override
    @Transient
    public java.lang.String toString() {
        return "false";
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$(){
        return $TypeDescriptor$;
    }
}
