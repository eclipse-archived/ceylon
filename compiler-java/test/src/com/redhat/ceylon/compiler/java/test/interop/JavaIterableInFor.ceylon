import java.lang{
    JString=String,
    JInteger=Integer,
    JIterable=Iterable
}


void javaIterableInFor() {
    JIterable<String> strings = nothing;
    JIterable<Integer> ints = nothing;
    JIterable<JString> jstrings = nothing;
    JIterable<JInteger> jints = nothing;
    
    variable value sum = 0;
    for (s in strings) {
        sum += s.hash;
    }
    for (s in jstrings) {
        sum += s.hash;
    }
    for (i in ints) {
        sum += i;
    }
    for (i in jints) {
        sum += i.hash;
    }
    // with an else
    for (i in jints) {
        if (i == 1) {
            break;
        }
        if (i == 2) {
            return;
        }
        if (i == 3) {
            throw;
        }
        sum += i.hash;
    } else {
        sum = 0;
    }
    /* not supported by the typechecker
    // entry and tuple patterns
    
    JIterable<String->Integer> stringsToInts = nothing;
    JIterable<JString->JInteger> jstringsToJints = nothing;
    for (s->i in stringsToInts) {
        sum += s.hash;
        sum += i.hash;
    }
    for (s->i in jstringsToJints) {
        sum += s.hash;
        sum += i.hash;
    }
    
    // tuple patterns
    JIterable<[String,Integer]> stringsInts = nothing;
    JIterable<[JString,JInteger]> jstringsJints = nothing;
    for ([s,i] in stringsInts) {
        sum += s.hash;
        sum += i.hash;
    }
    for ([s,i] in jstringsJints) {
        sum += s.hash;
        sum += i.hash;
    }
    */
    
}