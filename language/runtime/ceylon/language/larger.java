package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class larger {
	
    public static Comparison getLarger(){
        return Comparison.LARGER;
    }
}
