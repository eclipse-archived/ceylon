package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class makeArray {

    private makeArray() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>")
    public static <Element> Array<Element> makeArray(
    @Name("size")
    @TypeInfo("ceylon.language.Integer")
    final Integer size,
    @Name("init")
    @TypeInfo("ceylon.language.Callable<Element,ceylon.language.Integer>")
    final Callable<Element> init) {
        List<Element> list = new ArrayList<Element>();
        for (int i=0; i<size.value; i++) {
            Element elem = init.$call(Integer.instance(i));
            list.add(elem);
        }
        if (list.size() > 0) {
            return new ArrayOfSome<Element>(list);
        } else {
            return new ArrayOfNone<Element>();
        }
    }
}
