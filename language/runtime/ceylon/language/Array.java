package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({
    "ceylon.language.List<Element>",
    "ceylon.language.FixedSized<Element>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty|ceylon.language.Array<Element>>",
    "ceylon.language.Cloneable<ceylon.language.Array<Element>>"
})
public abstract class Array<Element> extends ArrayList<Element>
        implements FixedSized<Element> {
    
    @Ignore
    Array(Element[] array, long first) {
        super(array, first);
    }

    @Ignore
    Array(java.util.List<Element> list) {
        super(list);
    }

    @Ignore
    Array() {
        super();
    }
    
    void setItem(@Name("index") @TypeInfo("ceylon.language.Integer") long index, @Name("element") Element element) {
        long idx = index+first;
        if (idx>=0 && idx < array.length) {
            array[(int)idx] = element;
        }
    }

    @Override
    public Element getFirst() {
        if (getEmpty()) {
            return null;
        } else {
            return array[0];
        }
    }
    
    @Override
    public java.lang.String toString() {
        return "Array TODO";
    }
}

@Ignore
class EmptyArray<Element> extends Array<Element> implements None<Element> {

    public EmptyArray() {
        super();
    }
    
}

@Ignore
class NonemptyArray<Element> extends Array<Element> implements Some<Element> {

    public NonemptyArray(Element[] array, long first) {
        super(array, first);
    }

    public NonemptyArray(java.util.List<Element> list) {
        super(list);
    }

    @Override
    public FixedSized<? extends Element> getRest() {
        if (first+1==array.length) {
            return arrayOfNone.<Element>arrayOfNone();
        }
        else {
            return new NonemptyArray<Element>(array, first + 1);
        }
    }

}
