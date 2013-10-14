package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 6)
@Object
public final class operatingSystem_ {

	private static final operatingSystem_ value = new operatingSystem_();

	public static operatingSystem_ get_() {
		return value;
	}

	private final java.lang.String newline = System.lineSeparator();
	private final java.lang.String fileSeparator = System.getProperty("file.separator");
    private final java.lang.String pathSeparator = System.getProperty("path.separator");

	private operatingSystem_() {
	}

	public java.lang.String getName() {
		java.lang.String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			return "windows";
		} else if (os.indexOf("mac") >= 0) {
			return "mac";
		} else if (os.indexOf("linux") >= 0) {
			return "linux";
		} else if (os.indexOf("nix") >= 0 || os.indexOf("sunos") >= 0) {
			return "unix";
		} else {
			return "other";
		}
	}

	public java.lang.String getVersion() {
		return System.getProperty("os.version");
	}

	public java.lang.String getNewline() {
		return newline;
	}

	public java.lang.String getFileSeparator() {
		return fileSeparator;
	}

    public java.lang.String getPathSeparator() {
        return pathSeparator;
    }

	@Override
	public java.lang.String toString() {
		return "operating system [" + getName() + " / " + getVersion() + "]";
	}
}
