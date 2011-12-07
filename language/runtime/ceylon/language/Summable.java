package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other",
        satisfies="ceylon.language.Summable<Other>"))
public interface Summable<Other extends Summable<Other>> {
    public Other plus(@Name("other") Other number);
}
