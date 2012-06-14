package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Class(extendsType="ceylon.language.IdentifiableObject")
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceBuilder<Element> implements Sized {

    List<Element> list;
    
    public SequenceBuilder() {}
     
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public synchronized Iterable<? extends Element> getSequence() {
        if (list==null || list.isEmpty()) {
            return $empty.getEmpty();
        }
        else {
            return new ArraySequence<Element>(list);
        }
    }
    
    public final synchronized void append(@Name("element") Element element) {
    	if (list==null) {
    	    list = new ArrayList<Element>();
    	}
    	list.add(element);
    }
    
    public final synchronized void appendAll(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<Element>") 
    Iterable<? extends Element> elements) {
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
    }
    
    @Ignore
    public final void appendAll() {
        appendAll($empty.getEmpty());
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
