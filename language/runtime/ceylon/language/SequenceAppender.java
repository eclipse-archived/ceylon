package ceylon.language;

import java.util.ArrayList;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Class(extendsType="ceylon.language.SequenceBuilder<Element>")
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceAppender<Element> extends SequenceBuilder<Element> {
    
    public SequenceAppender(@Name("elements") 
    @TypeInfo("ceylon.language.Sequence<Element>")
    Sequence<? extends Element> elements) {
    	list = new ArrayList<Element>((int) elements.getSize()+2);
    	for (Iterator<? extends Element> iter=elements.getIterator(); iter!=null; iter=iter.getTail()) {
    		list.add(iter.getHead());
    	}
    }
    
    @Override
    @TypeInfo("ceylon.language.Sequence<Element>")
    public final synchronized Sequence<? extends Element> getSequence() {
        return new ArraySequence<Element>(list);
    }
    
}
