package ceylon.language;

import java.util.ArrayList;

import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 4)
@Class(extendsType="ceylon.language::SequenceBuilder<Element>")
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceAppender<Element> extends SequenceBuilder<Element> {
    
    @Ignore
    private TypeDescriptor $reifiedElement;

    public SequenceAppender(@Ignore TypeDescriptor $reifiedElement, 
            @Name("elements") 
            @TypeInfo("ceylon.language::Sequence<Element>")
            Sequence<? extends Element> elements) {
        super($reifiedElement);
    	list = new ArrayList<Element>((int) elements.getSize()+2);
        java.lang.Object elem;
        for (Iterator<? extends Element> iter=elements.getIterator(); !((elem = iter.next()) instanceof Finished);) {
    		list.add((Element) elem);
    	}
        this.$reifiedElement = $reifiedElement;
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public final Sequence<? extends Element> getSequence() {
        return new ArraySequence<Element>($reifiedElement, list);
    }
    
}
