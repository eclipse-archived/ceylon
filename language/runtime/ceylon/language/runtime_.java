package ceylon.language;


import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 6) @Object
@ceylon.language.SharedAnnotation$annotation$
public final class runtime_ {
    
    private static final runtime_ value = new runtime_();
    
    @ceylon.language.SharedAnnotation$annotation$
    public static runtime_ get_() {
        return value;
    }

    private final Integer maxIntegerValue = Integer.instance(Long.MAX_VALUE);
    
    private final Integer minIntegerValue = Integer.instance(Long.MIN_VALUE);
    
    private final Integer maxArraySize = Integer.instance(java.lang.Integer.MAX_VALUE - 8);
    
    private runtime_() {}
    

    public java.lang.String getName() {
        return "jvm";
    }
    
    public java.lang.String getVersion() {
        return System.getProperty("java.specification.version");
    }
    
    public long getIntegerSize() {
        return 64L;
    }

    public Integer getMinIntegerValue() {
        return minIntegerValue;
    }

    public Integer getMaxIntegerValue() {
        return maxIntegerValue;
    }
    
    public Integer getMaxArraySize() {
    	return maxArraySize;
    }
    
    @Override
    public java.lang.String toString() {
        return "runtime [" + getName() + " / " + getVersion() + "]";
    }
}
