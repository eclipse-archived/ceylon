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

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::IdentifiableObject")
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceBuilder<Element> implements Sized {

    java.util.List<Element> list;
    
    public SequenceBuilder() {}
     
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<Element>")
    public synchronized List<? extends Element> getSequence() {
        if (list==null || list.isEmpty()) {
            return (List)empty_.getEmpty$();
        }
        else {
            return new ArraySequence<Element>(list);
        }
    }
    
    public final synchronized SequenceBuilder<Element> append(@Name("element") Element element) {
    	if (list==null) {
    	    list = new ArrayList<Element>();
    	}
    	list.add(element);
    	return this;
    }
    
    public final synchronized SequenceBuilder<Element> appendAll(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language::Sequential<Element>") 
    List<? extends Element> elements) {
    	if (list==null) {
    	    //we don't always receive a Sized
    	    if (elements instanceof Sized) {
                list = new ArrayList<Element>((int) ((Sized) elements).getSize());
    	    } else {
                list = new ArrayList<Element>();
    	    }
    	}
    	java.lang.Object elem;
    	for (Iterator<? extends Element> iter=elements.getIterator(); !((elem = iter.next()) instanceof Finished);) {
    	    list.add((Element) elem);
    	}
    	return this;
    }
    
    @Ignore
    public final SequenceBuilder<Element> appendAll() {
        return this;
    }
    
    @Override
    public final synchronized long getSize() {
        return list==null ? 0 : list.size();
    }
     
    @Override
    public final synchronized boolean getEmpty() {
        return list==null ? true : list.isEmpty();
    }
     
}
