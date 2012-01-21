package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT, 
                               satisfies = "ceylon.language.Equality"))
@SatisfiedTypes({"ceylon.language.Collection<Element>",
                 "ceylon.language.Slots<ceylon.language.Set<ceylon.language.Equality>>",
                 "ceylon.language.Cloneable<ceylon.language.Set<Element>>"})
public interface Set<Element> 
        extends Collection<Element>, Slots<Set<Object>> {

}
