package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Element")
})
public class SequenceBuilder<Element> {

    final List<Element> list;
    
    public SequenceBuilder() {
    	this(new ArrayList<Element>());
    }
     
    SequenceBuilder(List<Element> list) {
    	this.list = list;
    }
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public synchronized Iterable<? extends Element> getSequence() {
        if (list.isEmpty()) {
            return $empty.getEmpty();
        }
        else {
            return new ArraySequence<Element>(list.toArray(), 0l);
        }
    }
    
    public synchronized void append(@Name("element") Element element) {
    	list.add(element);
    }
    
    public synchronized void appendAll(@Sequenced @Name("elements") 
    //@TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>") 
    Iterable<? extends Element> elements) {
    	for (Iterator<? extends Element> iter=elements.getIterator(); iter!=null; iter=iter.getTail()) {
    	    list.add(iter.getHead());
    	}
    }
     
}
