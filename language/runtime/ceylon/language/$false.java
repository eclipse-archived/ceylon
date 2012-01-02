package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class $false {
    private final static Boolean value = new Boolean("false"){
			@Override
			public boolean booleanValue() {
				return false;
			}
		};
    
    public static Boolean getFalse(){
        return value;
    }
}
