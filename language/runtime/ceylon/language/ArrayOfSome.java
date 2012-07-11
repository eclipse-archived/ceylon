package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;

@Ignore
@Ceylon(major = 2)
class ArrayOfSome<Element> extends Array<Element> implements Some<Element> {

    public ArrayOfSome(java.lang.Object array) {
        super(array);
    }
    
    @Override
    public FixedSized<? extends Element> getRest() {
        if (getSize() == 1) {
            return $arrayOfNone.<Element>arrayOfNone();
        } else {
            if (array instanceof char[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((char[])array, 1, (int)getSize()));
            } else if (array instanceof byte[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((byte[])array, 1, (int)getSize()));
            } else if (array instanceof short[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((short[])array, 1, (int)getSize()));
            } else if (array instanceof int[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((int[])array, 1, (int)getSize()));
            } else if (array instanceof long[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((long[])array, 1, (int)getSize()));
            } else if (array instanceof float[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((float[])array, 1, (int)getSize()));
            } else if (array instanceof double[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((double[])array, 1, (int)getSize()));
            } else if (array instanceof boolean[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((boolean[])array, 1, (int)getSize()));
            } else if (array instanceof java.lang.String[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((java.lang.String[])array, 1, (int)getSize()));
            } else {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((Element[])array, 1, (int)getSize()));
            }
        }
    }

    @Override
    public boolean getEmpty() {
        return false;
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
    public Array<? extends Element> getReversed() {
    	// TODO!!!!!!!!!!
    	return null;
    }

    /*@Override
    public FixedSized<? extends Element> getRest() {
        return span(Integer.instance(1), null);
    }*/
    
    @Override
    @Annotations({ @Annotation("actual") })
    public Element getFirst() {
        return unsafeItem(0);
    }
    @Override
    @Annotations({ @Annotation("actual") })
    public Element getLast() {
        final long s = getSize();
        return s > 0 ? unsafeItem((int)s-1) : null;
    }
    
    @Override 
    @Ignore 
    public Iterable<? extends Element> getSequence() { 
        return Iterable$impl._getSequence(this); 
    }

    @Override @Ignore public <Other>Array withLeading() { return this; }
    @Override @Ignore public <Other>Array withTrailing() { return this; }
    @Override @Ignore public <Other>Array withLeading$elements() { return this; }
    @Override @Ignore public <Other>Array withTrailing$elements() { return this; }

    @Override
    @Annotations({ @Annotation("actual") })
    public <Other> List withLeading(@Sequenced Iterable<? extends Other> elems) {
        return List$impl._withLeading(this, elems);
    }
    @Override
    @Annotations({ @Annotation("actual") })
    public <Other> List withTrailing(@Sequenced Iterable<? extends Other> elems) {
        return List$impl._withTrailing(this, elems);
    }
}
