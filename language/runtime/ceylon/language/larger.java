package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
public class larger extends Comparison {
	
	private larger() {
		super("larger");
	}
	
	private static final larger larger = new larger();
	
    public static larger getLarger(){
        return larger;
    }
}
