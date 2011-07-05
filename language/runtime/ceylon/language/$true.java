package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class $true {
    private final static Boolean value = new Boolean(String.instance("true")){
			@Override
			public boolean booleanValue() {
				return true;
			}
		}; 

    public static Boolean getTrue(){
        return value;
    }
}
