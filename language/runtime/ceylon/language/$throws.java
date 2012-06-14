package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Method
public class $throws
{
    private $throws(){}
    
    public static Nothing $throws(
    		@Name("type") @TypeInfo("ceylon.language.Void") 
    		final java.lang.Object type, 
    	    @Defaulted
    		@Name("when") @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
    		String when) {
        return null;
    }
    
    @Ignore
    public static Nothing $throws(final java.lang.Object type) {
        return $throws(type, $init$when(type));
    }
    @Ignore
    public static String $init$when(final java.lang.Object type) {
        return null;
    }
    
}
