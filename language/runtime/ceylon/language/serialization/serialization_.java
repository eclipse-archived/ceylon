package ceylon.language.serialization;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.runtime.serialization.SerializationContextImpl;

import ceylon.language.serialization.SerializationContext;

@Ceylon(major = 8, minor=0)
@Method
public class serialization_ {
    private serialization_(){}
    
    public static SerializationContext serialization() {
        return new SerializationContextImpl();
    }
}