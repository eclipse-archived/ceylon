class RangeIterationCorrect() {
    shared Integer[] integerRangeOp(Integer start, Integer end) {
        value b = ArrayBuilder<Integer>();
        for (i in start..end) {
            b.append(i);
        }
        return b.sequence();
    }
    shared Integer[] integerRange(Integer start, Integer end) {
        value b = ArrayBuilder<Integer>();
        variable Range<Integer> range = start..end;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }
    shared Character[] characterRangeOp(Character start, Character end) {
        value b = ArrayBuilder<Character>();
        for (i in start..end) {
            b.append(i);
        }
        return b.sequence();
    }
    shared Character[] characterRange(Character start, Character end) {
        value b = ArrayBuilder<Character>();
        variable Range<Character> range = start..end;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }
    shared Element[] tpRange<Element>(Element start, Element end)
            given Element satisfies Enumerable<Element> {
        value b = ArrayBuilder<Element>();
        variable Range<Element> range = start..end;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }
    shared Element[] tpRangeNoOpt<Element>(Element start, Element end)
            given Element satisfies Enumerable<Element> {
        value b = ArrayBuilder<Element>();
        variable Range<Element> range = start..end;
        @disableOptimization
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }
    //--------------------------------
    shared Integer[] integerSegmentOp(Integer start, Integer length) {
        value b = ArrayBuilder<Integer>();
        for (i in start:length) {
            b.append(i);
        }
        return b.sequence();
    }
    /*shared Integer[] integerSegment(Integer start, Integer length) {
        value b = ArrayBuilder<Integer>();
        variable SizedRange<Integer> range = start:length;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }*/
    shared Character[] characterSegmentOp(Character start, Integer length) {
        value b = ArrayBuilder<Character>();
        for (i in start:length) {
            b.append(i);
        }
        return b.sequence();
    }
    /*shared Character[] characterSegment(Character start, Integer length) {
        value b = ArrayBuilder<Character>();
        variable SizedRange<Character> range = start:length;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }*/
    /*shared Element[] tpSegment<Element>(Element start, Integer length) 
            given Element satisfies Enumerable<Element> {
        value b = ArrayBuilder<Element>();
        variable SizedRange<Element> range = start:length;
        for (i in range) {
            b.append(i);
        }
        return b.sequence();
    }*/
}
abstract class Cyclic(shared actual String string)
        of foo|bar|baz 
        satisfies Enumerable<Cyclic> {
}
object foo extends Cyclic("foo") {
    shared actual Integer offset(Cyclic other) {
        switch (other)
        case (foo) {
            return 0;
        }
        case (bar) {
            return 2;
        }
        case (baz) {
            return 1;
        }
    }
    shared actual Cyclic neighbour(Integer offset) {
        switch (((offset % 3) + 3) % 3) 
        case (0) {
            return foo;
        }
        case (1) {
            return bar;
        }
        case (2) {
            return baz;
        }
        else {
            throw;
        }
    }
}
object bar extends Cyclic("bar") {
    shared actual Integer offset(Cyclic other) {
        switch (other)
        case (foo) {
            return 1;
        }
        case (bar) {
            return 0;
        }
        case (baz) {
            return 2;
        }
    }
    
    shared actual Cyclic neighbour(Integer offset) {
        switch (((offset % 3) + 3) % 3) 
        case (0) {
            return bar;
        }
        case (1) {
            return baz;
        }
        case (2) {
            return foo;
        }
        else {
            throw;
        }
    }
}
object baz extends Cyclic("baz") {
    shared actual Integer offset(Cyclic other) {
        switch (other)
        case (foo) {
            return 2;
        }
        case (bar) {
            return 1;
        }
        case (baz) {
            return 0;
        }
    }
    
    shared actual Cyclic neighbour(Integer offset) {
        switch (((offset % 3) + 3) % 3) 
        case (0) {
            return baz;
        }
        case (1) {
            return foo;
        }
        case (2) {
            return bar;
        }
        else {
            throw;
        }
    }
}
abstract class Finite(shared actual String string) 
    of top|middle|bottom
    satisfies Enumerable<Finite> {
    
}
object top extends Finite("top") {
    shared actual Finite neighbour(Integer off) {
        switch (off) 
        case (0) {
            return this;
        }
        case (-1) {
            return middle;
        }
        case (-2) {
            return bottom;
        }
        else {
            return off > 0 then top else bottom;
        }
    }
    shared actual Integer offset(Finite other) {
        switch (other) 
        case (top) {
            return 0;
        }
        case (middle) {
            return 1;
        }
        case (bottom) {
            return 2;
        }
    }
}
object middle extends Finite("middle") {
    shared actual Finite neighbour(Integer off) {
        switch (off) 
        case (0) {
            return this;
        }
        case (-1) {
            return bottom;
        }
        case (1) {
            return top;
        }
        else {
            return off > 0 then top else bottom;
        }
    }
    shared actual Integer offset(Finite other) {
        switch (other) 
        case (top) {
            return -1;
        } 
        case (middle) {
            return 0;
        } 
        case (bottom) {
            return 1;
        }
    }
}
object bottom extends Finite("bottom") {
    shared actual Finite neighbour(Integer off) {
        switch (off) 
        case (0) {
            return this;
        } 
        case (1) {
            return middle;
        } 
        case (2) {
            return top;
        }
        else {
            return off > 0 then top else bottom;
        }
    }
    shared actual Integer offset(Finite other) {
        switch (other) 
        case (top) {
            return -2;
        } 
        case (middle) {
            return -1;
        } 
        case (bottom) {
            return 0;
        }
    }
}
void rangeIterationCorrect() {
    // First let's sanity check out enumerable implementations
    assert([foo] == foo..foo);
    assert([bar] == bar..bar);
    assert([baz] == baz..baz);
    assert([foo, bar] == foo..bar);
    assert([bar, baz] == bar..baz);
    assert([baz, foo] == baz..foo);
    assert([foo, bar, baz] == foo..baz);
    assert([bar, baz, foo] == bar..foo);
    assert([baz, foo, bar] == baz..bar);
    
    assert([bottom] == bottom..bottom);
    assert([middle] == middle..middle);
    assert([top] == top..top);
    assert([bottom, middle, top] == bottom..top);
    assert([top, middle, bottom] == top..bottom);
    assert([bottom, middle] == bottom..middle);
    assert([middle, bottom] == middle..bottom);
    assert([middle, top] == middle..top);
    assert([top, middle] == top..middle);
    
    value inst = RangeIterationCorrect();
    assert(inst.integerRangeOp(0, 10) == 0..10);
    assert(inst.integerRange(0, 10) == 0..10);
    assert(inst.characterRangeOp('a', 'z') == 'a'..'z');
    assert(inst.characterRange('a', 'z') == 'a'..'z');
    assert(inst.tpRange(0, 10) == 0..10);
    
    assert(inst.tpRange(foo, foo) == [foo]);
    assert(inst.tpRange(bar, bar) == [bar]);
    assert(inst.tpRange(baz, baz) == [baz]);
    assert(inst.tpRange(foo, bar) == [foo, bar]);
    assert(inst.tpRange(bar, baz) == [bar, baz]);
    assert(inst.tpRange(baz, foo) == [baz, foo]);
    assert(inst.tpRange(foo, baz) == [foo, bar, baz]);
    assert(inst.tpRange(bar, foo) == [bar, baz, foo]);
    assert(inst.tpRange(baz, bar) == [baz, foo, bar]);
    
    assert(inst.tpRange(bottom, bottom) == [bottom]);
    assert(inst.tpRange(middle, middle) == [middle]);
    assert(inst.tpRange(top, top) == [top]);
    assert(inst.tpRange(top, bottom) == [top, middle, bottom]);
    assert(inst.tpRange(bottom, top) == [bottom, middle, top]);
    assert(inst.tpRange(top, middle) == [top, middle]);
    assert(inst.tpRange(middle, top) == [middle, top]);
    assert(inst.tpRange(middle, bottom) == [middle, bottom]);
    assert(inst.tpRange(bottom, middle) == [bottom, middle]);
}