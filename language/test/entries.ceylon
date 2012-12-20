Entry<Integer,String> entry = Entry<Integer,String> {
    item="hello";
    key=1;
};

Range<Integer> range = Range<Integer> {
    first=-3;
    last=+10;
};

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
    value e = entries("a", "b", "c", "X", "Y", "Z", "1", "2", "3", "d", "e", "f").sequence;
    value _e = Entry(-1, "null");
    check((e[2] else _e).key==2, "entries [1]");
    check((e[2] else _e).item=="c", "entries [2]");
    check(1->"a" == 1->"a", "entry.equals [1]");
    check(1->"a" != 1->"b", "entry.equals [2]");
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
    check(e.string=="1->hello", "entry string");
    check(e.key==1, "entry key");
    check(e.item=="hello", "entry item");
    check(entry==1->"hello", "entry equals");
    check(entry!=2->"hello", "entry equals");
    check(entry!=1->"bye", "entry equals");
    
    check(range.size==14, "range size");
    check(range.lastIndex==13, "range last index");
    check(range.string=="-3..10", "range string");
    check(range.first==-3, "range first");
    check(range.last==+10, "range last");
    check(!range.decreasing, "range decreasing");
    check(range==-3..+10, "range equals");
    check(range[5] exists, "range element");
    check(!range[14] exists, "range element");
    Integer[] r = range;
    check(r nonempty, "range nonempty");
    
    if (exists el=r[0]) {
        check(el==-3, "range first element");
    }
    else {
        fail("range first element");
    }
    
    if (exists el=r[13]) {
        check(el==+10, "range last element");
    }
    else {
        fail("range last element");
    }
    
    if (exists el=r[5]) {
        check(el==2, "range element");
    }
    else {
        fail("range element");
    }
    
    if (exists el=r[14]) {
        fail("out of range element");
    }
    
    variable value j:=0;
    for (i in range) {
        check(i+3==j++, "range iteration");
    }
    check(j==14, "range iteration");
    
    j:=+10;
    for (i in 10..0) {
        check(i==j--, "decreasing range iteration");
    }
    check(j==-1, "decreasing range iteration");
    
    String->Entry<Boolean,String> ent = "hello"->(true->"hello");
    check(ent.item.item=="hello", "entry item item");
    
    check((0..10).segment(2, 3).string=="2..4", "range segment");
    check((0..10).span(2, 5).string=="2..5", "range span");
    check((2..10).segment(1, 3).string=="3..5", "range segment");
    check((2..10).span(2, 7).string=="4..9", "range span");
    check(!(0..9).segment(11,10) nonempty, "(0..9).segment(11,10) is empty");
    check(!(0..9).segment(3,0) nonempty, "(0..9).segment(3,0) is empty");
    check(!(0..9).span(11,12) nonempty, "(0..9).span(11,12) is NOT empty");
    check((0..9).span(5,3) nonempty, "(0..9).span(5,3) is empty");
    
    check((1..1).by(5).sequence.string=="{ 1 }", "range by 5");
    check((0..9).by(1).sequence.string=="0..9", "range by 1");
    check((0..9).by(3).sequence.string=="{ 0, 3, 6, 9 }", "range by 3");
    check((2..11).by(3).sequence.string=="{ 2, 5, 8, 11 }", "range by 3");
    check((0..9).by(4).sequence.string=="{ 0, 4, 8 }", "range by 4");
    check((2..11).by(4).sequence.string=="{ 2, 6, 10 }", "range by 4");
    
    //More range tests, from ceylon-js
    check((1..10).string=="1..10", "range.string");
    value r1= 1..5;
    check(r1.size==5, "range.size 1");
    value r2 = 7..4;
    check(r2.size==4, "range.size 2");
    value r3 = -10..-5;
    check(r3.size==6, "range.size 3");
    value r4 = 123..123;
    check(r4.size==1, "range.size 4");
    check(r1.includes(3), "range.includes 1");
    check(!r1.includes(6), "range.includes 2");
    check(r2.includes(5), "range.includes 3");
    check(!r2.includes(3), "range.includes 4");
    check(r4.first==r4.last, "range first == last");
    check(r1 != r2, "range.equals 1");
    check(r1 == 1..5, "range.equals 2");
    value r1r = r1.rest;
    value r2r = r2.rest;
    check(r1r.size==r1.size-1, "range.rest.size 1");
    check(r2r.size==r2.size-1, "range.rest.size 2");
    check((r1r.first else 0) == (r1.item(1) else 1), "range.rest.first 1");
    check((r2r.first else 0) == (r2.item(1) else 1), "range.rest.first 2");
    check(!r4.rest nonempty, "nonempty range.rest");
    check(r1.lastIndex==4, "range.lastIndex 1");
    check(r2.lastIndex==3, "range.lastIndex 2");
    check(r1.by(2).sequence.string=="{ 1, 3, 5 }", "range.by 1");
    check(r1.by(3).sequence.string=="{ 1, 4 }", "range.by 2");
    check(r2.by(2).sequence.string=="{ 7, 5 }", "range.by 3");
    check(r2.by(3).sequence.string=="{ 7, 4 }", "range.by 4");
    check(r4.by(10).sequence.string=="{ 123 }", "range.by 5");
    check(r1.segment(2,2).string=="3..4", "range.segment 1");
    check(!r1.segment(1,0) nonempty, "range.segment 2");
    check(r1.segment(1,-1).empty, "range.segment 3");
    check(r1.segment(3,1).string=="4..4", "range.segment 4");
    check(r1.segment(0,1).string=="1..1", "range.segment 5");
    check(r1.span(1, 3).string=="2..4", "range.span 1");
    check(r1.span(3, 1).string=="4..2", "range.span 2");
    check(r1.span(2, 2).string=="3..3", "range.span 3");
    check(r1.spanFrom(3).string=="4..5", "range.spanFrom 4");
    check(r1.spanTo(3).string=="1..4", "range.spanTo 4");
    check(r1.span(3, 1000).string=="4..5", "range.span 5");
    check(r1.span(0,0).string=="1..1", "range.span 6");
    check(!(1..2).span(4,5) nonempty, "range.span (out of bounds)");
    check(r1.definesEvery(1,2,3), "range.definesEvery 1");
    check(!r1.definesEvery(4,5,6,7), "range.definesEvery 2");
    check(r1.definesAny(1,2,3), "range.definesAny 1");
    check(!r1.definesAny(7,6,5), "range.definesAny 2");
    check(r1.definesAny(6,5,4), "range.definesAny 3");
    variable Integer sum := 0;
    for (Integer x in r1) {
        sum += x;
    }
    check(sum==15, "range iteration");
    check(1 in 1..5, "range in 1");
    check(3 in 1..5, "range in 2");
    check(5 in 1..5, "range in 3");
    check(!0 in 1..5, "range in 4");
    check(!6 in 1..5, "range in 5");
    check(!2..3 in 1..5, "range in 6");
    
    check(!(1..5).span(-1,-2) nonempty, "empty range [1]");
    check(!(1..5).span(-2,-1) nonempty, "empty range [2]");
    check(!(1..5).span(6,8) nonempty, "empty range [3]");
    check(!(1..5).span(8,6) nonempty, "empty range [4]");
    check(r1[...2] == { 1, 2, 3 }, "r1[...2]");
    check(r1[3...] == { 4, 5 }, "r1[3...]");
    check(r1[...-1] == {}, "r1[...-1] " r1[...-1] "");

    //non-Integer Ranges
    class TestRange(Integer number) satisfies Ordinal<TestRange> & Comparable<TestRange> {
        shared actual Integer distanceFrom(TestRange other) {
            return other.number-number;
        }
        shared actual TestRange predecessor { return TestRange(number-1); }
        shared actual TestRange successor { return TestRange(number+1); }
        shared actual Comparison compare(TestRange other) {
            return number <=> other.number;
        }
        shared actual String string { return "TestRange(" number ")"; }
    }
    sum:=0;
    for (t in TestRange(1)..TestRange(5)) {
        sum++;
    }
    check(sum==5, "Range with Ordinal [1]");
    for (t in (TestRange(1)..TestRange(4)).by(2)) {
        sum++;
    }
    check(sum==7, "Range with Ordinal [2]");

    //Test the entries function
    test_entries_function();
    //Test comparisons by Key and Item
    value e1 = entries("a", "B", "c", "D");
    value k1 = e1.sort(byKey((Integer a, Integer b) b<=>a)).sequence;
    value k2 = e1.sort(byItem((String a, String b) a<=>b)).sequence;
    if (exists x=k1[0]) {
        check(x==3->"D", "byKey[1]");
    } else { fail("byKey[1]"); }
    if (exists x=k1[3]) {
        check(x==0->"a", "byKey[2]");
    } else { fail("byKey[2]"); }
    if (exists x=k2[0]) {
        check(x==1->"B", "byItem[1]");
    } else { fail("byItem[1]"); }
    if (exists x=k2[3]) {
        check(x==2->"c", "byItem[2]");
    } else { fail("byItem[2]"); }
    if (exists x=e1.find(forKey((Integer k) k==2))) {
        check(x == 2->"c", "forKey [1]");
    } else { fail("forKey [2]"); }
    if (exists x=e1.find(forItem((String s) s=="B"))) {
        check(x == 1->"B", "forItem [1]");
    } else { fail("forItem [2]"); }
    check(e1.count(forItem((String s) s<"a"))==2, "forItem [3]");
    check(e1.map(forKey((Integer k) k.string.repeat(3))).sequence == {"000","111","222","333"}, "forKey [3]");
}
