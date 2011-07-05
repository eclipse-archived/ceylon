package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class $false {
    private final static Boolean value = new Boolean(String.instance("false")){
			@Override
			public boolean booleanValue() {
				return false;
			}
		};
    
    public static Boolean getFalse(){
        return value;
    }
}
