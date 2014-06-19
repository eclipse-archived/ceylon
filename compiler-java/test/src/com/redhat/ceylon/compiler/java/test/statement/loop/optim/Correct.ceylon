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
            satisfies Enumerable<MyOrdinal> {
        shared actual MyOrdinal neighbour(Integer n)
            => MyOrdinal(i+n);
        shared actual Integer offset(MyOrdinal other) {
            return i-other.i;
        }
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
    
    function tupleIterationStatic() {
        [Integer, Integer, Integer*] array = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        variable value sum = 0;
        for (i in array) {
            sum+=i;
        }
        return sum;
    }
    assert(expected == tupleIterationStatic());
    
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
    
    
}