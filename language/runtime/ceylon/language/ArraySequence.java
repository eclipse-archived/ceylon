package ceylon.language;

import java.util.Arrays;
import java.util.List;

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
    ArraySequence(List<Element> list) {
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
    public ceylon.language.List<? extends Element> span(Integer from, Integer to) {
    	long fromIndex = from.longValue();
    	if (fromIndex<0) fromIndex=0;
    	long toIndex = to==null ? array.length-1 : to.longValue();
        long lastIndex = getLastIndex().longValue();
		if (fromIndex>lastIndex||toIndex<fromIndex) {
            return $empty.getEmpty();
        }
        else if (toIndex>lastIndex) {
            return new ArraySequence<Element>(array, fromIndex);
        }
        else {
            return new ArraySequence<Element>(Arrays.copyOfRange(array, 
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
            return $empty.getEmpty();
        }
        else if (fromIndex+resultLength>lastIndex) {
            return new ArraySequence<Element>(array, fromIndex);
        }
        else {
            return new ArraySequence<Element>(Arrays.copyOfRange(array, 
            		(int)fromIndex, (int)(fromIndex + resultLength)), 0);
        }
    }
    
    @Override
    public java.lang.String toString() {
        return List$impl.toString(this);
    }

}