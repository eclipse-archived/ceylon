Entry<Natural,String> entry {
    item="hello";
    key=1;
}

Range<Integer> range {
    first=-3;
    last=+10;
}

void entriesAndRanges() {
    
    Entry<Equality, Equality> e = entry;
    assert(e.string=="1->hello", "entry string");
    assert(e.key==1, "entry key");
    assert(e.item=="hello", "entry item");
    assert(entry==1->"hello", "entry equals");
    assert(entry!=2->"hello", "entry equals");
    assert(entry!=1->"bye", "entry equals");
    
    assert(range.size==14, "range size");
    assert(range.lastIndex==13, "range last index");
    assert(range.string=="-3..10", "range string");
    assert(range.first==-3, "range first");
    assert(range.last==+10, "range last");
    assert(!range.decreasing, "range decreasing");
    assert(range==-3..+10, "range equals");
    assert(exists range[5], "range element");
    assert(!exists range[14], "range element");
    Integer[] r = range;
    assert(nonempty r, "range nonempty");
    
    if (exists el=r[0]) {
        assert(el==-3, "range first element");
    }
    else {
        fail("range first element");
    }
    
    if (exists el=r[13]) {
        assert(el==+10, "range last element");
    }
    else {
        fail("range last element");
    }
    
    if (exists el=r[5]) {
        assert(el==2, "range element");
    }
    else {
        fail("range element");
    }
    
    if (exists el=r[14]) {
        fail("out of range element");
    }
    
    variable value j:=+0;
    for (i in range) {
        assert(i+3==j++, "range iteration");
    }
    assert(j==14, "range iteration");
    
    j:=+10;
    for (i in 10..0) {
        assert(i==j--, "decreasing range iteration");
    }
    assert(j==-1, "decreasing range iteration");
    
    String->Entry<Boolean,String> ent = "hello"->(true->"hello");
    assert(ent.item.item=="hello", "entry item item");
    
    assert((0..10).segment(2, 3).string=="2..4", "range segment");
    assert((0..10).span(2, 5).string=="2..5", "range span");
    assert((2..10).segment(1, 3).string=="3..5", "range segment");
    assert((2..10).span(2, 7).string=="4..9", "range span");
    assert(!nonempty (0..9).segment(11,10), "empty range segment");
    assert(!nonempty (0..9).segment(3,0), "empty range segment");
    assert(!nonempty (0..9).span(11,12), "empty range span");
    assert(!nonempty (0..9).span(5,3), "empty range span"); //TODO: this is wrong
                                                                                                                                
}