package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@CaseTypes({"ceylon.language.Some", 
            "ceylon.language.None"})
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface FixedSized<Element> 
        extends Collection<Element> {
    @TypeInfo("ceylon.language.Nothing|Element")
    public Element getFirst();
}
