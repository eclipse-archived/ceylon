package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Other",
    		satisfies="ceylon.language::Binary<Other>"))
@CaseTypes(of = "Other")
public interface Binary<Other extends Binary<Other>> {

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other getNot();

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public long getSize();

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other leftLogicalShift(@Name("shift") long shift);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other rightLogicalShift(@Name("shift") long shift);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other rightArithmeticShift(@Name("shift") long shift);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other and(@Name("other") Other other);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other or(@Name("other") Other other);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other xor(@Name("other") Other other);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public boolean get(@Name("index") long index);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other set(@Name("index") long index, 
                     @Name("bit") @Defaulted boolean bit);

    @Ignore
    public boolean set$bit(long index);

    @Ignore
    public Other set(long index);

    @Annotations({@Annotation("shared"), @Annotation("default")})
    public Other clear(@Name("index") long index);

    @Annotations({@Annotation("shared"), @Annotation("formal")})
    public Other flip(@Name("index") long index);
}
