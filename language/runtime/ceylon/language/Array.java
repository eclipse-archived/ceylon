package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class Array<Element> extends ArraySequence<Element> {
    
    public Array(Element... array) {
        super(array);
    }

    public static <Element> Array<Element> instance(Element[] array) {
        return new Array<Element>(array);
    }
    
    /*
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> java.lang.Object instance(Element[] array) {
        if (array.length > 0) {
            return new Array<Element>(array);
        } else {
            return $empty.getEmpty();
        }
    }
    */
}