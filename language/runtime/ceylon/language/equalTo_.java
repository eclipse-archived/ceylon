package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public class equalTo_ {
    private equalTo_(){}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
    public static <Element extends Comparable<? super Element>> Callable<? extends Boolean> equalTo(
            @Name("val") @TypeInfo("Element")
            final Element val) {
        return new AbstractCallable<Boolean>("equalTo"){
            public Boolean $call(java.lang.Object element) {
                return Boolean.instance(val.equals(element));
            }
        };
    }
}
