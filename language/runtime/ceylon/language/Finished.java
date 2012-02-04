package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon
@Class(extendsType = "ceylon.language.IdentifiableObject")
@CaseTypes("ceylon.language.exhausted")
public abstract class Finished {
}
