package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon(major = 3)
@CaseTypes({"ceylon.language.Unit", 
	        "ceylon.language.Tuple<ceylon.language.Void,ceylon.language.TupleOrUnit>"})
public interface TupleOrUnit {}
