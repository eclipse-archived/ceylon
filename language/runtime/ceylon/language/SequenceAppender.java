package ceylon.language;

import java.util.ArrayList;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Element")
})
public class SequenceAppender<Element> extends SequenceBuilder<Element> {
    
    public SequenceAppender(@Name("elements") 
    @TypeInfo("ceylon.language.Sequence<Element>")
    Sequence<? extends Element> elements) {
    	super();
    	list = new ArrayList<Element>((int) elements.getSize()+2);
    	for (Iterator<? extends Element> iter=elements.getIterator(); iter!=null; iter=iter.getTail()) {
    		list.add(iter.getHead());
    	}
    }
    
    @Override
    @TypeInfo("ceylon.language.Sequence<Element>")
    public final synchronized Sequence<? extends Element> getSequence() {
        return new ArraySequence<Element>(list.toArray(), 0l);
    }
    
}
