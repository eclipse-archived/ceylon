Entry<Integer,String> entry {
    item="hello";
    key=1;
}

Range<Integer> range {
    first=-3;
    last=+10;
}

/*class Holder() {
shared Entry<Integer,String> entry {
    item="hello";
    key=1;
}

shared Range<Integer> range {
    first=-3;
    last=+10;
}
}*/

void test_entries_function() {
    value e = entries("a", "b", "c", "X", "Y", "Z", "1", "2", "3", "d", "e", "f");
    value _e = Entry(-1, "null");
    assert((e.item(2)?_e).key==2, "entries");
    assert((e.item(2)?_e).item=="c", "entries");
    assert(1->"a" == 1->"a", "entry.equals");
    assert(1->"a" != 1->"b", "entry.equals");
}

void entriesAndRanges() {
    
/*Entry<Integer,String> entry {
    item="hello";
    key=1;
}

Range<Integer> range {
    first=-3;
    last=+10;
}*/

    Entry<Object, Object> e = entry;
    //Entry<Equality, Equality> e = Holder().entry;
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
    
    variable value j:=0;
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
    assert(!nonempty (0..9).segment(11,10), "(0..9).segment(11,10) is empty");
    assert(!nonempty (0..9).segment(3,0), "(0..9).segment(3,0) is empty");
    assert(!nonempty (0..9).span(11,12), "(0..9).span(11,12) is NOT empty");
    assert(nonempty (0..9).span(5,3), "(0..9).span(5,3) is empty");
    
    assert((1..1).by(5).string=="1..1", "range by");
    assert((0..9).by(1).string=="0..9", "range by 1");
    assert((0..9).by(3).string=="{ 0, 3, 6, 9 }", "range by");
    assert((2..11).by(3).string=="{ 2, 5, 8, 11 }", "range by");
    assert((0..9).by(4).string=="{ 0, 4, 8 }", "range by");
    assert((2..11).by(4).string=="{ 2, 6, 10 }", "range by");

    //More range tests, from ceylon-js
    assert((1..10).string=="1..10", "range.string");
    value r1= 1..5;
    assert(r1.size==5, "range.size 1");
    value r2 = 7..4;
    assert(r2.size==4, "range.size 2");
    value r3 = -10..-5;
    assert(r3.size==6, "range.size 3");
    value r4 = 123..123;
    assert(r4.size==1, "range.size 4");
    assert(r1.includes(3), "range.includes 1");
    assert(!r1.includes(6), "range.includes 2");
    assert(r2.includes(5), "range.includes 3");
    assert(!r2.includes(3), "range.includes 4");
    assert(r4.first==r4.last, "range first == last");
    assert(r1 != r2, "range.equals 1");
    assert(r1 == 1..5, "range.equals 2");
    value r1r = r1.rest;
    value r2r = r2.rest;
    assert(r1r.size==r1.size-1, "range.rest.size 1");
    assert(r2r.size==r2.size-1, "range.rest.size 2");
    assert(r1r.first?0 == r1.item(1)?1, "range.rest.first 1");
    assert(r2r.first?0 == r2.item(1)?1, "range.rest.first 2");
    assert(nonempty r4.rest, "nonempty range.rest");
    assert(r1.lastIndex==4, "range.lastIndex 1");
    assert(r2.lastIndex==3, "range.lastIndex 2");
    assert(r1.by(2).string=="{ 1, 3, 5 }", "range.by 1");
    assert(r1.by(3).string=="{ 1, 4 }", "range.by 2");
    assert(r2.by(2).string=="{ 7, 5 }", "range.by 3");
    assert(r2.by(3).string=="{ 7, 4 }", "range.by 4");
    assert(r4.by(10).string=="123..123", "range.by 5");
    assert(r1.segment(2,2).string=="3..4", "range.segment 1");
    assert(!nonempty r1.segment(1,0), "range.segment 2");
    assert(r1.segment(1,-1).empty, "range.segment 3");
    assert(r1.segment(3,1).string=="4..4", "range.segment 4");
    assert(r1.segment(0,1).string=="1..1", "range.segment 5");
    assert(r1.span(1, 3).string=="2..4", "range.span 1");
    assert(r1.span(3, 1).string=="4..2", "range.span 2");
    assert(r1.span(2, 2).string=="3..3", "range.span 3");
    assert(r1.span(3, null).string=="4..5", "range.span 4");
    assert(r1.span(3, 1000).string=="4..5", "range.span 5");
    assert(r1.span(0,0).string=="1..1", "range.span 6");
    assert(!nonempty (1..2).span(4,5), "range.span (out of bounds)");
    assert(r1.definesEvery(1,2,3), "range.definesEvery 1");
    assert(!r1.definesEvery(4,5,6,7), "range.definesEvery 2");
    assert(r1.definesAny(1,2,3), "range.definesAny 1");
    assert(!r1.definesAny(7,6,5), "range.definesAny 2");
    assert(r1.definesAny(6,5,4), "range.definesAny 3");
    variable Integer sum := 0;
    for (Integer x in r1) {
        sum += x;
    }
    assert(sum==15, "range iteration");
    assert(1 in 1..5, "range in 1");
    assert(3 in 1..5, "range in 2");
    assert(5 in 1..5, "range in 3");
    assert(!0 in 1..5, "range in 4");
    assert(!6 in 1..5, "range in 5");
    assert(!2..3 in 1..5, "range in 6");

    //Test the entries function
    test_entries_function();
}
