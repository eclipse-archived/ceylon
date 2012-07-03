package ceylon.language;

import java.util.ArrayList;
import java.util.List;

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
public final class coalesce {

    private coalesce() {}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Iterable<? extends Element> coalesce(
    @Name("values") @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Nothing|Element>")
    final ceylon.language.Iterable<? extends Element> values) {
        if (values instanceof ArraySequence) {
            Element[] arr = ((ArraySequence<Element>)values).toArray();
            int lnn = arr.length;
            for (int i=arr.length-1; i>=0; i--) {
                if (arr[i] == null) {
                    int j=i-1;
                    while (j>=0 && arr[j]==null) j--;
                    if (j<0) j=0;
                    if (arr[j] != null) {
                        lnn = i;
                        arr[i]=arr[j];
                        arr[j]=null;
                    }   
                } else lnn=i;
            }
            return lnn==arr.length ? (Iterable)$empty.getEmpty() : new ArraySequence(arr, lnn);
        }
        final class NotNullIterator implements Iterator<Element> {
            private final Iterator<? extends Element> orig = values.getIterator();
            @Override public java.lang.Object next() {
                java.lang.Object tmp = null;
                while ((tmp = orig.next()) == null);
                return tmp;
            }
        }
        return new AbstractIterable<Element>() {
            @Override public Iterator<? extends Element> getIterator() { return new NotNullIterator(); }
        }.getSequence();
    }
    @Ignore
    public static <Element> Iterable<? extends Element> coalesce() {
        return (Iterable<? extends Element>) $empty.getEmpty();
    }
}
