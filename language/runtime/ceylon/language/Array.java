 package ceylon.language;

import java.util.Arrays;
import java.util.List;

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
    public ceylon.language.List<? extends Element> span(Integer from, Integer to) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to==null ? array.length-1 : to.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||toIndex<fromIndex) {
            return arrayOfNone.arrayOfNone();
        }
        else if (toIndex>lastIndex) {
            return new NonemptyArray<Element>(array, fromIndex);
        }
        else {
            return new NonemptyArray<Element>(Arrays.copyOfRange(array, 
                    (int)fromIndex, (int)toIndex+1), 0);
        }
    }
    
    @Override
    public ceylon.language.List<? extends Element> segment(Integer from, Integer length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength==0) {
            return arrayOfNone.arrayOfNone();
        }
        else if (fromIndex+resultLength>lastIndex) {
            return new NonemptyArray<Element>(array, fromIndex);
        }
        else {
            return new NonemptyArray<Element>(Arrays.copyOfRange(array, 
                    (int)fromIndex, (int)(fromIndex + resultLength)), 0);
        }
    }
    
    @Override
    public java.lang.String toString() {
        return "Array TODO";
    }
}

class EmptyArray<Element> extends Array<Element> implements None<Element> {

    public EmptyArray() {
        super();
    }
    
}

class NonemptyArray<Element> extends Array<Element> implements Some<Element> {

    public NonemptyArray(Element[] array, long first) {
        super(array, first);
    }

    public NonemptyArray(List<Element> list) {
        super(list);
    }

    @Override
    public FixedSized<? extends Element> getRest() {
        if (first+1==array.length) {
            return arrayOfNone.arrayOfNone();
        }
        else {
            return new NonemptyArray<Element>(array, first + 1);
        }
    }

}
