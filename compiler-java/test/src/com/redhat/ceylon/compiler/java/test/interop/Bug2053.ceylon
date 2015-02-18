import java.lang{Number,Long}

@noanno
shared void bug2053<Element>(
        Bug2053Varargs<Long> x,
        {Element*} items)
        given Element satisfies Object {
    
    x.unbounded(*items);
    
    assert(is {Number*} items);
    x.bound(*items);
    
    assert(is {Long*} items);
    x.indirectBound(*items);
    
    {Left&Right*} items3 = nothing;
    x.bound2(*items3).right();
}