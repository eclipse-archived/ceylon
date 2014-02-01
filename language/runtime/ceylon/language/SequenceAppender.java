package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class(extendsType="ceylon.language::SequenceBuilder<Element>")
@TypeParameters(@TypeParameter(value = "Element"))
@ceylon.language.SharedAnnotation$annotation$
public class SequenceAppender<Element> 
        extends SequenceBuilder<Element> {
    
    public SequenceAppender(@Ignore TypeDescriptor $reifiedElement, 
            @Name("elements") 
            @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Nothing>")
            Iterable<? extends Element, ?> elements) {
        super($reifiedElement);
        appendAll(elements);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public final Sequence<? extends Element> getSequence() {
        return (Sequence<? extends Element>)super.getSequence();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SequenceAppender.class, $reifiedElement);
    }
    
    @Override
    public SequenceAppender<Element> append(@Name("element") Element element) {
    	super.append(element);
    	return this;
    }
    
    @Override
    public SequenceAppender<Element> appendAll(@Name("elements") 
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>") 
    Iterable<? extends Element, ?> elements) {
    	super.appendAll(elements);
    	return this;
    }
    
}
