package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Other",
    		satisfies="ceylon.language.Subtractable<Other,Inverse>"),
    @TypeParameter(value = "Inverse")
})
@SatisfiedTypes({"ceylon.language.Numeric<Other>",
	             "ceylon.language.Invertable<Inverse>"})
public interface Subtractable<Other extends Subtractable<Other,Inverse>,Inverse> 
    extends Numeric<Other>, Invertable<Inverse> {
    
    public Inverse minus(@Name("other") Other number);
    
}
