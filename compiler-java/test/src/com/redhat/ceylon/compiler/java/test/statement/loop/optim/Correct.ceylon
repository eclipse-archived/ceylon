import java.lang{IntArray, arrays}

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
        
        IntArray array = arrays.toIntArray(1..10);
        variable value sum = 0;
        for (i in array.array) {
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