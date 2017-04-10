package ceylon.language;

import com.redhat.ceylon.common.NonNull;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Method
@NativeAnnotation$annotation$(backends={})
final class arrayToTuple_ {
    @SuppressWarnings("unchecked")
    @NonNull
    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language::Sequence<Element>")
    public static <Element> Sequence<? extends Element> arrayToTuple(
            @Ignore TypeDescriptor $reifiedElement,
            @Name("array") @NonNull
            @TypeInfo("ceylon.language::Array<Element>")
            Array<Element> array) {
        if (array.getEmpty()) {
            throw new AssertionError("array must not be empty");
        }
        return (Sequence<? extends Element>) 
                Tuple.instance($reifiedElement, 
                        array.toObjectArray());
    }
    
    private arrayToTuple_(){}
}
