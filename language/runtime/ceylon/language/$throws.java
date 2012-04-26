package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public class $throws
{
    public static Nothing $throws(
    		@Name("type") @TypeInfo("ceylon.language.Void") 
    		final Object type, 
    	    @Defaulted
    		@Name("when") @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
    		java.lang.String when) {
        return null;
    }
    private $throws(){}
    
    @Ignore
    public static java.lang.String $init$when(final Object type) {
        return null;
    }
    
}
