package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

@Ceylon(major = 7) 
@Object
@Class(extendsType = "ceylon.language::Boolean")
@ValueType
public final class true_ extends Boolean {
    
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
}
