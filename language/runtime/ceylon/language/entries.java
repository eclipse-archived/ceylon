package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class entries {
    
    private entries() {}
    
    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Entry<ceylon.language.Integer,Element&ceylon.language.Object>>")
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries(@Name("elements")
    @Sequenced @TypeInfo("ceylon.language.Iterable<Element>")
    final ceylon.language.Iterable<? extends Element> elements) {
        final class EntryIterator implements Iterator<Entry<? extends Integer, ? extends Element>> {
            private long i=0;
            private final Iterator<? extends Element> orig = elements.getIterator();
            @Override public java.lang.Object next() {
                java.lang.Object tmp = null;
                while ((tmp = orig.next()) == null);
                return tmp == exhausted.getExhausted() ? tmp : new Entry<Integer, Element>(Integer.instance(i++), (Element)tmp);
            }
            
        }
        return new AbstractIterable<Entry<? extends Integer,? extends Element>>() {
            @Override
            public Iterator<? extends Entry<? extends Integer, ? extends Element>> getIterator() {
                return new EntryIterator();
            }
        };
    }
    @Ignore
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries() {
        return (Iterable) $empty.getEmpty();
    }
}
