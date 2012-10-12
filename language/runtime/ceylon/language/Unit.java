package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon(major = 3)
@SatisfiedTypes("ceylon.language.TupleOrUnit")
public interface Unit extends TupleOrUnit {}
