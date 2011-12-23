package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
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
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>") 
    Iterable<? extends Element> elements) {
    	if (list==null) {
    	    list = new ArrayList<Element>( (int) ((Sized) elements).getSize() );
    	}
    	for (Iterator<? extends Element> iter=elements.getIterator(); iter!=null; iter=iter.getTail()) {
    	    list.add(iter.getHead());
    	}
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
