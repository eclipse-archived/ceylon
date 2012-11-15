package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class join_ {
    
    private join_() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public static <Element> List<? extends Element> join(
    @Name("iterables")
    @Sequenced
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Iterable<Element>>")
    final List<? extends Iterable<? extends Element>> iterables) {
        java.util.List<Element> list = new java.util.ArrayList<Element>();
		java.lang.Object $tmp1;
		for (Iterator<? extends Iterable<? extends Element>> iter=iterables.getIterator(); 
				!(($tmp1 = iter.next()) instanceof Finished);) {
		    final Iterable<? extends Element> elem = (Iterable<? extends Element>) $tmp1;
	        java.lang.Object $tmp2;
			for (Iterator<? extends Element> iter2=elem.getIterator(); 
					!(($tmp2 = iter2.next()) instanceof Finished);) {
			    final Element elem2 = (Element) $tmp2;
				if (elem2 != null) list.add(elem2);
			}
		}
        if (list.isEmpty()) {
            return (List<? extends Element>) empty_.getEmpty$();
        }
        else {
            return new ArraySequence<Element>(list);
        }
    }
    @Ignore
    public static <Element> List<? extends Element> join() {
        return join((List)empty_.getEmpty$());
    }
}
