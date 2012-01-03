package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other",
        satisfies="ceylon.language.Summable<Other>"))
public interface Summable<Other extends Summable<Other>> {
    public Other plus(@Name("other") Other number);
}
