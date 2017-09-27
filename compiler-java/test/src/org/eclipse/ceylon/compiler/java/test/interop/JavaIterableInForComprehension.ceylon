import java.lang{
    JString=String,
    JInteger=Integer,
    JIterable=Iterable
}
import java.util{
    Arrays{
        asList
    }
}

void assertSameElements<T>(JIterable<T> j, Iterable<T> c) given T satisfies Object {
    value jit = j.iterator();
    value cit = c.iterator();
    while (true) {
        if (jit.hasNext()) {
            value je = jit.next();
            assert(!is Finished ce=cit.next());
            assert(je == ce);
        } else {
            assert(is Finished ce=cit.next());
            break;
        }
    }
}

alias Strings=>JIterable<String>;
alias JStrings=>JIterable<JString>;

@noanno
void javaIterableInForComprehension() {
    JIterable<String> strings = asList("a", "b", "c");
    JIterable<Integer> ints = asList(1, 2, 3);
    JIterable<JString> jstrings = asList(JString("a"), JString("b"), JString("c"));
    JIterable<JInteger> jints = asList(JInteger(1), JInteger(2), JInteger(3));
    
    assertSameElements(strings, {for (s in strings) s});
    assertSameElements(jstrings, {for (s in jstrings) s});
    assertSameElements(ints, {for (s in ints) s});
    assertSameElements(jints, {for (s in jints) s});
    
    // via type alias
    assertSameElements((strings of Strings), {for (s in strings) s});
    assertSameElements((jstrings of JStrings), {for (s in jstrings) s});
    
    // filters
    assert([1,3] == {for (x in ints) if(x%2==1) x}.sequence());
    
    // products
    assert([1,2,3,2,4,6,3,6,9] == {for (x in ints) for (y in ints) x*y}.sequence());
    
    //value c6 = {for (s in jstrings) for (c in s) s.hash + c.hash};
    
    // with if
    // non-initial for
    // runtime tests
}