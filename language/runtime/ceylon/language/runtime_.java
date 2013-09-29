package ceylon.language;


import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 5) @Object
public final class runtime_ {
    
    private static final runtime_ value = new runtime_();
    
    public static runtime_ $get() {
        return value;
    }

    private final Integer maxIntegerValue = Integer.instance(Long.MAX_VALUE);
    
	private final Integer minIntegerValue = Integer.instance(Long.MIN_VALUE);
	
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
    
    @Override
    public java.lang.String toString() {
    	return "runtime [" + getName() + " / " + getVersion() + "]";
    }
}
