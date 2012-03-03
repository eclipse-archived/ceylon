package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ignore
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class ArraySequence<Element> extends ArrayList<Element> implements Sequence<Element> {

    public ArraySequence(Element... array) {
        this(array,0);
    }
    
    @Ignore
    ArraySequence(Element[] array, long first) {
        super(array, first);
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
    }

    @Ignore
    ArraySequence(java.util.List<Element> list) {
        super(list);
    	if (array.length==0) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
    }

    @Override
    public Element getFirst() {
        return array[(int) first];
    }

    @Override
    public FixedSized<? extends Element> getRest() {
        if (first+1==array.length) {
            return $empty.getEmpty();
        }
        else {
            return new ArraySequence<Element>(array, first + 1);
        }
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

    @Override
    public Element getLast() {
        return array[array.length - 1];
    }
    
    @Override
    public java.lang.String toString() {
        return List$impl.toString(this);
    }

}