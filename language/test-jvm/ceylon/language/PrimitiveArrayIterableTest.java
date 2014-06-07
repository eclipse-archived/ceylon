package ceylon.language;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.IntArray;
import com.redhat.ceylon.compiler.java.language.IntArray.IntArrayIterable;
import com.redhat.ceylon.compiler.java.language.ObjectArray;
import com.redhat.ceylon.compiler.java.language.ObjectArray.ObjectArrayIterable;

public class PrimitiveArrayIterableTest {

    @Test
    public void testIntArrayIterable() {
        IntArrayIterable zero_ten = IntArray.getIterable(new int[]{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}).skip(1).take(11);
        IntArrayIterable zero_five = IntArray.getIterable(new int[]{0, 1, 2, 3, 4, 5});
        IntArrayIterable five_ten = IntArray.getIterable(new int[]{5, 6, 7, 8, 9, 10});
        IntArrayIterable empty = IntArray.getIterable(new int[]{});
        IntArrayIterable ten = IntArray.getIterable(new int[]{10});
        IntArrayIterable zero = IntArray.getIterable(new int[]{0});
        
        // getSize
        Assert.assertEquals(11, zero_ten.getSize());
        Assert.assertEquals(6, zero_five.getSize());
        Assert.assertEquals(6, five_ten.getSize());
        Assert.assertEquals(0, empty.getSize());
        Assert.assertEquals(1, ten.getSize());
        Assert.assertEquals(1, zero.getSize());
        
        // getEmpty
        Assert.assertFalse(zero_ten.getEmpty());
        Assert.assertFalse(zero_five.getEmpty());
        Assert.assertFalse(five_ten.getEmpty());
        Assert.assertTrue(empty.getEmpty());
        Assert.assertFalse(ten.getEmpty());
        Assert.assertFalse(zero.getEmpty());
        
        // getFirst
        Assert.assertEquals(0, zero_ten.getFirst().value);
        Assert.assertEquals(10, ten.getFirst().value);
        Assert.assertEquals(0, zero.getFirst().value);
        Assert.assertEquals(null, empty.getFirst());
        
        // getLast
        Assert.assertEquals(10, zero_ten.getLast().value);
        Assert.assertEquals(10, ten.getLast().value);
        Assert.assertEquals(0, zero.getLast().value);
        Assert.assertEquals(null, empty.getLast());
        
        // getRest
        Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", 
                zero_ten.getRest().sequence().toString());
        Assert.assertEquals(1, zero_ten.getRest().getFirst().value);
        Assert.assertEquals(10, zero_ten.getRest().getLast().value);
        Assert.assertEquals("[2, 3, 4, 5, 6, 7, 8, 9, 10]", 
                zero_ten.getRest().getRest().sequence().toString());
        Assert.assertEquals("[]", ten.getRest().sequence().toString());
        Assert.assertEquals("[]", ten.getRest().getRest().sequence().toString());
        
        // getSequence
        Assert.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", zero_ten.sequence().toString());
        
        // longerThan
        Assert.assertTrue(zero_ten.longerThan(10));
        Assert.assertFalse(zero_ten.longerThan(11));
        Assert.assertFalse(zero_ten.longerThan(12));
        
        // shorterThan
        Assert.assertFalse(zero_ten.shorterThan(10));
        Assert.assertFalse(zero_ten.shorterThan(11));
        Assert.assertTrue(zero_ten.shorterThan(12));

        // iterator
        Iterator<? extends Integer> iterator = zero_ten.iterator();
        Assert.assertEquals(ceylon.language.Integer.instance(0), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(1), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(2), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(3), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(4), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(5), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(6), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(7), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(8), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(9), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(10), iterator.next());
        Assert.assertEquals(ceylon.language.finished_.get_(), iterator.next());
        Assert.assertEquals(ceylon.language.finished_.get_(), iterator.next());
        
        // containsAny
        Assert.assertTrue(zero_ten.containsAny(zero_ten));
        Assert.assertTrue(zero_ten.containsAny(zero));
        Assert.assertTrue(zero_ten.containsAny(ten));
        Assert.assertTrue(zero_ten.containsAny(zero_five));
        Assert.assertTrue(zero_ten.containsAny(five_ten));
        Assert.assertFalse(zero_ten.containsAny(empty));
        Assert.assertTrue(zero_five.containsAny(zero_ten));
        
        // containsEvery
        Assert.assertTrue(zero_ten.containsEvery(zero_ten));
        Assert.assertTrue(zero_ten.containsEvery(zero));
        Assert.assertTrue(zero_ten.containsEvery(ten));
        Assert.assertTrue(zero_ten.containsEvery(empty));
        Assert.assertTrue(zero_ten.containsEvery(zero_five));
        Assert.assertTrue(zero_ten.containsEvery(five_ten));
        Assert.assertFalse(zero_five.containsEvery(zero_ten));
        Assert.assertFalse(five_ten.containsEvery(zero_ten));
        
        // any
        class BetweenPred extends AbstractCallable<ceylon.language.Boolean> {

            private final long lower;
            private final long upper;
            
            public BetweenPred(long lower, long upper) {
                super(ceylon.language.Boolean.$TypeDescriptor$, 
                        ceylon.language.Integer.$TypeDescriptor$, "Boolean(Integer)", (short)-1);
                this.lower = lower;
                this.upper = upper;
            }
            
            public ceylon.language.Boolean $call$(java.lang.Object arg0) {
                ceylon.language.Integer arg = (ceylon.language.Integer)arg0;
                boolean result = this.lower <= arg.value
                        && arg.value <= this.upper;
                return ceylon.language.Boolean.instance(result);
            }
        }
        BetweenPred between3And4 = new BetweenPred(3,4);
        Assert.assertTrue(zero_ten.any(between3And4));
        Assert.assertTrue(zero_five.any(between3And4));
        Assert.assertFalse(five_ten.any(between3And4));
        Assert.assertFalse(zero.any(between3And4));
        Assert.assertFalse(ten.any(between3And4));

        // contains
        Assert.assertTrue(zero_ten.contains(ceylon.language.Integer.instance(0)));
        Assert.assertTrue(zero_ten.contains(ceylon.language.Integer.instance(10)));
        Assert.assertFalse(zero_ten.contains(ceylon.language.Integer.instance(11)));
        Assert.assertFalse(zero_ten.contains(ceylon.language.Integer.instance(-1)));
        
        // taking
        IntArrayIterable zero_seven = zero_ten.take(8);
        Assert.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7]", zero_seven.sequence().toString());
        
        // skipping
        IntArrayIterable two_seven = zero_ten.take(8).skip(2);
        Assert.assertEquals("[2, 3, 4, 5, 6, 7]", two_seven.sequence().toString());
        
        // by
        IntArrayIterable zero_ten_evens = zero_ten.by(2);
        Assert.assertEquals("[0, 2, 4, 6, 8, 10]", zero_ten_evens.sequence().toString());
        Assert.assertEquals(6, zero_ten_evens.getSize());
        Assert.assertTrue(zero_ten_evens.longerThan(5));
        Assert.assertFalse(zero_ten_evens.longerThan(6));
        Assert.assertFalse(zero_ten_evens.longerThan(7));
        
        Assert.assertFalse(zero_ten_evens.shorterThan(5));
        Assert.assertFalse(zero_ten_evens.shorterThan(6));
        Assert.assertTrue(zero_ten_evens.shorterThan(7));
        
        Assert.assertEquals("[0, 4, 8]", zero_ten_evens.by(2).sequence().toString());
        Assert.assertEquals(3, zero_ten_evens.by(2).getSize());
        
        Assert.assertEquals("[2, 4, 6, 8]", zero_ten_evens.take(5).skip(1).sequence().toString());
        
    }
    
    ObjectArrayIterable<ceylon.language.Integer> ints(int... ints) {
        ceylon.language.Integer[] result = new ceylon.language.Integer[ints.length];
        for (int ii = 0; ii < ints.length; ii++) {
            result[ii] = ceylon.language.Integer.instance(ints[ii]);
        }
        return ObjectArray.<ceylon.language.Integer>getIterable(result);
    }
    
    @Test
    public void testObjectArrayIterable() {
        ObjectArrayIterable<ceylon.language.Integer> zero_ten = ints(-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).skip(1).take(11);
        //ObjectArrayIterable<ceylon.language.Integer> zero_ten = ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ObjectArrayIterable<ceylon.language.Integer> zero_five = ints(0, 1, 2, 3, 4, 5);
        ObjectArrayIterable<ceylon.language.Integer> five_ten = ints(5, 6, 7, 8, 9, 10);
        ObjectArrayIterable<ceylon.language.Integer> empty = ints();
        ObjectArrayIterable<ceylon.language.Integer> ten = ints(10);
        ObjectArrayIterable<ceylon.language.Integer> zero = ints(0);
        
        // getSize
        Assert.assertEquals(11, zero_ten.getSize());
        Assert.assertEquals(6, zero_five.getSize());
        Assert.assertEquals(6, five_ten.getSize());
        Assert.assertEquals(0, empty.getSize());
        Assert.assertEquals(1, ten.getSize());
        Assert.assertEquals(1, zero.getSize());
        
        // getEmpty
        Assert.assertFalse(zero_ten.getEmpty());
        Assert.assertFalse(zero_five.getEmpty());
        Assert.assertFalse(five_ten.getEmpty());
        Assert.assertTrue(empty.getEmpty());
        Assert.assertFalse(ten.getEmpty());
        Assert.assertFalse(zero.getEmpty());
        
        // getFirst
        Assert.assertEquals(Integer.instance(0), zero_ten.getFirst());
        Assert.assertEquals(Integer.instance(10), ten.getFirst());
        Assert.assertEquals(Integer.instance(0), zero.getFirst());
        Assert.assertEquals(null, empty.getFirst());
        
        // getLast
        Assert.assertEquals(Integer.instance(10), zero_ten.getLast());
        Assert.assertEquals(Integer.instance(10), ten.getLast());
        Assert.assertEquals(Integer.instance(0), zero.getLast());
        Assert.assertEquals(null, empty.getLast());
        
        // getRest
        Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", 
                zero_ten.getRest().sequence().toString());
        Assert.assertEquals(Integer.instance(1), zero_ten.getRest().getFirst());
        Assert.assertEquals(Integer.instance(10), zero_ten.getRest().getLast());
        Assert.assertEquals("[2, 3, 4, 5, 6, 7, 8, 9, 10]", 
                zero_ten.getRest().getRest().sequence().toString());
        Assert.assertEquals("[]", ten.getRest().sequence().toString());
        Assert.assertEquals("[]", ten.getRest().getRest().sequence().toString());
        
        // getSequence
        Assert.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", zero_ten.sequence().toString());
        
        // longerThan
        Assert.assertTrue(zero_ten.longerThan(10));
        Assert.assertFalse(zero_ten.longerThan(11));
        Assert.assertFalse(zero_ten.longerThan(12));
        
        // shorterThan
        Assert.assertFalse(zero_ten.shorterThan(10));
        Assert.assertFalse(zero_ten.shorterThan(11));
        Assert.assertTrue(zero_ten.shorterThan(12));

        // iterator
        Iterator<? extends Integer> iterator = zero_ten.iterator();
        Assert.assertEquals(ceylon.language.Integer.instance(0), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(1), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(2), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(3), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(4), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(5), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(6), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(7), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(8), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(9), iterator.next());
        Assert.assertEquals(ceylon.language.Integer.instance(10), iterator.next());
        Assert.assertEquals(ceylon.language.finished_.get_(), iterator.next());
        Assert.assertEquals(ceylon.language.finished_.get_(), iterator.next());
        
        // containsAny
        Assert.assertTrue(zero_ten.containsAny(zero_ten));
        Assert.assertTrue(zero_ten.containsAny(zero));
        Assert.assertTrue(zero_ten.containsAny(ten));
        Assert.assertTrue(zero_ten.containsAny(zero_five));
        Assert.assertTrue(zero_ten.containsAny(five_ten));
        Assert.assertFalse(zero_ten.containsAny(empty));
        Assert.assertTrue(zero_five.containsAny(zero_ten));
        
        // containsEvery
        Assert.assertTrue(zero_ten.containsEvery(zero_ten));
        Assert.assertTrue(zero_ten.containsEvery(zero));
        Assert.assertTrue(zero_ten.containsEvery(ten));
        Assert.assertTrue(zero_ten.containsEvery(empty));
        Assert.assertTrue(zero_ten.containsEvery(zero_five));
        Assert.assertTrue(zero_ten.containsEvery(five_ten));
        Assert.assertFalse(zero_five.containsEvery(zero_ten));
        Assert.assertFalse(five_ten.containsEvery(zero_ten));
        
        // any
        class BetweenPred extends AbstractCallable<ceylon.language.Boolean> {

            private final long lower;
            private final long upper;
            
            public BetweenPred(long lower, long upper) {
                super(ceylon.language.Boolean.$TypeDescriptor$, 
                        ceylon.language.Integer.$TypeDescriptor$, "Boolean(Integer)", (short)-1);
                this.lower = lower;
                this.upper = upper;
            }
            
            public ceylon.language.Boolean $call$(java.lang.Object arg0) {
                ceylon.language.Integer arg = (ceylon.language.Integer)arg0;
                boolean result = this.lower <= arg.value
                        && arg.value <= this.upper;
                return ceylon.language.Boolean.instance(result);
            }
        }
        BetweenPred between3And4 = new BetweenPred(3,4);
        Assert.assertTrue(zero_ten.any(between3And4));
        Assert.assertTrue(zero_five.any(between3And4));
        Assert.assertFalse(five_ten.any(between3And4));
        Assert.assertFalse(zero.any(between3And4));
        Assert.assertFalse(ten.any(between3And4));

        // contains
        Assert.assertTrue(zero_ten.contains(ceylon.language.Integer.instance(0)));
        Assert.assertTrue(zero_ten.contains(ceylon.language.Integer.instance(10)));
        Assert.assertFalse(zero_ten.contains(ceylon.language.Integer.instance(11)));
        Assert.assertFalse(zero_ten.contains(ceylon.language.Integer.instance(-1)));
        
        // taking
        ObjectArrayIterable<Integer> zero_seven = zero_ten.take(8);
        Assert.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7]", zero_seven.sequence().toString());
        
        // skipping
        ObjectArrayIterable<Integer> two_seven = zero_ten.take(8).skip(2);
        Assert.assertEquals("[2, 3, 4, 5, 6, 7]", two_seven.sequence().toString());
        
        // by
        ObjectArrayIterable<Integer> zero_ten_evens = zero_ten.by(2);
        Assert.assertEquals("[0, 2, 4, 6, 8, 10]", zero_ten_evens.sequence().toString());
        Assert.assertEquals(6, zero_ten_evens.getSize());
        Assert.assertTrue(zero_ten_evens.longerThan(5));
        Assert.assertFalse(zero_ten_evens.longerThan(6));
        Assert.assertFalse(zero_ten_evens.longerThan(7));
        
        Assert.assertFalse(zero_ten_evens.shorterThan(5));
        Assert.assertFalse(zero_ten_evens.shorterThan(6));
        Assert.assertTrue(zero_ten_evens.shorterThan(7));
        
        Assert.assertEquals("[0, 4, 8]", zero_ten_evens.by(2).sequence().toString());
        Assert.assertEquals(3, zero_ten_evens.by(2).getSize());
        
        Assert.assertEquals("[2, 4, 6, 8]", zero_ten_evens.take(5).skip(1).sequence().toString());
    }
    
}
