package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class larger {
	
    public static Comparison getLarger(){
        return Comparison.LARGER;
    }
}
