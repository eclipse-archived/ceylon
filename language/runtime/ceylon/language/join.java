package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class join {
    
    private join() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Iterable<? extends Element> join(
    @Name("sequences")
    @Sequenced
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Empty|ceylon.language.Sequence<Element>>")
    final ceylon.language.Iterable<? extends ceylon.language.Iterable<? extends Element>> sequences) {
		List<Element> list = new ArrayList<Element>();
		for (Iterator<? extends Iterable<? extends Element>> iter=sequences.getIterator(); 
				iter!=null; iter=iter.getTail()) {
			Iterable<? extends Element> elem = iter.getHead();
			for (Iterator<? extends Element> iter2=elem.getIterator(); 
					iter2!=null; iter2=iter2.getTail()) {
				if (elem!=null) list.add(iter2.getHead());
			}
		}
        return new ArraySequence<Element>(list);
    }
}
