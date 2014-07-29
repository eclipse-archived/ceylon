package ceylon.language;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.Transient;

@Ceylon(major = 7) @Object
public final class system_ {
    
    private static final system_ value = new system_();
    
    public static system_ get_() {
        return value;
    }
    
    private system_() {}
	
    public long getMilliseconds() {
    	return System.currentTimeMillis();
    }
    
    public long getNanoseconds() {
        return System.nanoTime();
    }
     
    public int getTimezoneOffset() {
        return TimeZone.getDefault().getOffset(getMilliseconds());
    }
    
    public java.lang.String getLocale() {
        return Locale.getDefault().toLanguageTag();
    }
    
    public java.lang.String getCharacterEncoding() {
        return Charset.defaultCharset().name();
    }
    
    @Override
    @Transient
    public java.lang.String toString() {
    	return "system";
    }
}
