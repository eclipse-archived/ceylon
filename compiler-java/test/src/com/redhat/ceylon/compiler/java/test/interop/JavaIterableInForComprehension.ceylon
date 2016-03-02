import java.lang{
    JString=String,
    JInteger=Integer,
    JIterable=Iterable
}

@noanno
void javaIterableInForComprehension() {
    JIterable<String> strings = nothing;
    JIterable<Integer> ints = nothing;
    JIterable<JString> jstrings = nothing;
    JIterable<JInteger> jints = nothing;
    
    value c1 = {for (s in strings) s.hash};
    value c2 = {for (s in strings) for (i in ints) s.hash + i};
    value c3 = {for (s in strings) for (c in s) s.hash + c.hash};
    
    value c4 = {for (s in jstrings) s.hash};
    value c5 = {for (s in jstrings) for (i in jints) s.hash + i.intValue()};
    //value c6 = {for (s in jstrings) for (c in s) s.hash + c.hash};
}