Integer echo(Integer x) {
    print(x);
    return x;
}

@noanno
void bug2304If() {
    value values = { 100, 110, 120 };
    value funcs = {
        for (v in values)
        if (exists w = true then (()=>v))
        w
    };
    value iter = funcs.sequence().iterator();
    assert(is Integer() x = iter.next(), 
        100 == echo(x()));
    assert(is Integer() y = iter.next(), 
        110 == echo(y()));
    assert(is Integer() z = iter.next(), 
        120 == echo(z()));
    assert(iter.next() is Finished);
}

void bug2304For() {
    value values = { 100, 110, 120 };
    value funcs = {
        for (v in values)
        for (w in { ()=>v })
        w
    };
    value iter = funcs.sequence().iterator();
    assert(is Integer() x = iter.next(), 
        100 == echo(x()));
    assert(is Integer() y = iter.next(), 
        110 == echo(y()));
    assert(is Integer() z = iter.next(), 
        120 == echo(z()));
    assert(iter.next() is Finished);
}
@noanno
void bug2304() {
    bug2304If();
    bug2304For(); 
}