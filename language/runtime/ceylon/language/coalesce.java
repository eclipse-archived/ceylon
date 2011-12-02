package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@Method
public final class coalesce {
    
    private coalesce() {}
    
    @TypeParameters({
    @TypeParameter(value="Element", satisfies="ceylon.language.Object")
    })
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element>ceylon.language.Iterable<? extends Element> coalesce(@Name("sequence")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Nothing|Element>")
    final ceylon.language.Iterable<? extends Element> sequence) {
		List<Element> list = new ArrayList<Element>();
		for (Iterator<? extends Element> iter=sequence.getIterator(); iter!=null; iter=iter.getTail()) {
			Element elem = iter.getHead();
			if (elem!=null) list.add(elem);
		}
        return new ArraySequence<Element>(list.toArray(), 0);
    }
}
