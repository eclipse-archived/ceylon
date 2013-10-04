package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Attribute;

@Ceylon(major = 5) @Attribute
public class minIntegerValue_ {

	public static Integer get_() {
		return Integer.instance(Long.MIN_VALUE);
	}
}
