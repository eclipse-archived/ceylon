package ceylon.language;

import java.util.ArrayList;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Class
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceBuilder<Element> implements ReifiedType {

    java.util.List<Element> list;
    @Ignore
    private TypeDescriptor $reifiedElement;
    
    public SequenceBuilder(@Ignore TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
     
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getSequence() {
        if (list==null || list.isEmpty()) {
            return (Sequential)empty_.getEmpty$();
        }
        else {
            return new ArraySequence<Element>($reifiedElement, list);
        }
    }
    
    public final SequenceBuilder<Element> append(@Name("element") Element element) {
    	if (list==null) {
    	    list = new ArrayList<Element>();
    	}
    	list.add(element);
    	return this;
    }
    
    public final SequenceBuilder<Element> appendAll(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language::Sequential<Element>") 
    Sequential<? extends Element> elements) {
    	if (list==null) {
    	    //we don't always receive an Iterable
    	    if (elements instanceof Iterable) {
                list = new ArrayList<Element>((int) elements.getSize());
    	    } else {
                list = new ArrayList<Element>();
    	    }
    	}
    	java.lang.Object elem;
    	for (Iterator<? extends Element> iter=elements.iterator(); !((elem = iter.next()) instanceof Finished);) {
    	    list.add((Element) elem);
    	}
    	return this;
    }
    
    @Ignore
    public final SequenceBuilder<Element> appendAll() {
        return this;
    }
    
    public final long getSize() {
        return list==null ? 0 : list.size();
    }
     
    public final boolean getEmpty() {
        return list==null ? true : list.isEmpty();
    }
     
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(SequenceBuilder.class, $reifiedElement);
    }
}
