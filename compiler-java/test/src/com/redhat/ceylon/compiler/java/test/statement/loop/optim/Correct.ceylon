import java.lang{IntArray}

class Correct() {
    value expected = 55;
    
    function notOptimizedIteration() {
        variable value sum = 0;
        @disableOptimization
        for (i in 1..10) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == notOptimizedIteration());
    
    function rangeOpIteration() {
        variable value sum = 0;
        for (i in 1..10) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == rangeOpIteration());
    
    function rangeOpIterationReverse() {
        variable value sum = 0;
        for (i in 10..1) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == rangeOpIterationReverse());
    
    function rangeIteration() {
        variable value sum = 0;
        Range<Integer> range = 1..10;
        for (i in range) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == rangeIteration());
    
    function rangeIterationReverse() {
        variable value sum = 0;
        Range<Integer> range = 1..10;
        for (i in range) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == rangeIterationReverse());
    
    class MyOrdinal(shared Integer i)
            satisfies Ordinal<MyOrdinal> & Comparable<MyOrdinal> {
        shared actual Comparison compare(MyOrdinal other)
                => i <=> other.i;
        shared actual MyOrdinal successor
                => MyOrdinal(i+1);
        shared actual MyOrdinal predecessor
                => MyOrdinal(i-1);
    }
    
    function steppedRangeIteration() {
        variable value sum = 0;
        Range<MyOrdinal> range = MyOrdinal(1)..MyOrdinal(10);
        for (i in range.by(1)) {
            sum+=i.i;
        }
        return sum;
    }
    assert(expected == steppedRangeIteration());
    
    function steppedRangeIterationReverse() {
        variable value sum = 0;
        Range<MyOrdinal> range = MyOrdinal(10)..MyOrdinal(1);
        for (i in range.by(1)) {
            sum+=i.i;
        }
        return sum;
    }
    assert(expected == steppedRangeIterationReverse());
    
    function arrayIterationStatic() {
        Array<Integer> array = Array(0..10);
        variable value sum = 0;
        for (i in array) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == arrayIterationStatic());
    
    function javaArrayIterationStatic() {
        
        IntArray array = IntArray(10);
        for (index in 1..10) {
            array.set(index-1, index);
        }
        variable value sum = 0;
        for (i in array.iterable) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == javaArrayIterationStatic());
    
    function arraySequenceIterationStatic() {
        assert(is ArraySequence<Integer> array = {for (i in 0..10) i}.sequence);
        variable value sum = 0;
        for (i in array) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == arraySequenceIterationStatic());
    
    
    function iterationDynamic(Iterable<Integer> iterable) {
        variable value sum = 0;
        for (i in iterable) {
            sum+=i;
        }
        return sum;
    }
    
    function rangeIterationDynamic() {
        Range<Integer> iterable = 0..10;
        return iterationDynamic(iterable);
    }
    assert(expected == rangeIterationDynamic());
    
    function arrayIterationDynamic() {
        Array<Integer> iterable = Array(0..10);
        return iterationDynamic(iterable);
    }
    assert(expected == arrayIterationDynamic());
    
    function arraySequenceIterationDynamic() {
     assert(is ArraySequence<Integer> iterable = {for (i in 0..10) i}.sequence);
        return iterationDynamic(iterable);
    }
    assert(expected == arraySequenceIterationDynamic());
    
}