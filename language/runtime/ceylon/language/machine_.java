package ceylon.language;


import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 5) @Object
public final class machine_ {
    
    private static final machine_ value = new machine_();
    
    public static machine_ $get() {
        return value;
    }

    private final Integer maxIntegerValue = Integer.instance(Long.MAX_VALUE);
    
	private final Integer minIntegerValue = Integer.instance(Long.MIN_VALUE);
	
    private machine_() {}
	

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
    	return "machine [" + getName() + " / " + getVersion() + "]";
    }
}
