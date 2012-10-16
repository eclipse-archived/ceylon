package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({"ceylon.language::FixedSized<Element>", 
                 "ceylon.language::ContainerWithFirstElement<ceylon.language::Bottom,ceylon.language::Nothing>"})
public interface None<Element> extends FixedSized<Element> {
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing")
    @Override
    public Element getFirst();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing")
    @Override
    public Element getLast();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Iterator<Element>")
    @Override
    public Iterator<? extends Element> getIterator();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public long getSize();
    
    @Annotations(@Annotation("actual"))
    @Override
    public boolean getEmpty();

}
