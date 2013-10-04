package ceylon.language;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 5) @Object
public final class language_ {
	
    public java.lang.String getVersion() {
        return Versions.CEYLON_VERSION_NUMBER;
    }
    
    public long getMajorVersion() {
        return Versions.CEYLON_VERSION_MAJOR;
    }

    public long getMinorVersion() {
        return Versions.CEYLON_VERSION_MINOR;
    }

    public long getReleaseVersion() {
        return Versions.CEYLON_VERSION_RELEASE;
    }

    public java.lang.String getVersionName() {
        return Versions.CEYLON_VERSION_NAME;
    }
    
    public long getMajorVersionBinary() {
        return Versions.JVM_BINARY_MAJOR_VERSION;
    }

    public long getMinorVersionBinary() {
        return Versions.JVM_BINARY_MINOR_VERSION;
    }

    @Override
    public java.lang.String toString() {
    	return "language";
    }
    
    private language_() {}
    private static final language_ value = new language_();
    
    public static language_ get_() {
        return value;
    }
}
