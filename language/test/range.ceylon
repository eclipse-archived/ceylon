@test
shared void testRange() {
    // TODO RangeOp
    
    check((1..2).rest == 2..2);
    check((2..2).rest == {});
    
    // SegmentOp
    variable value x = 0:-1;
    check(x.size == 0, "0:-1 size");
    check(!x nonempty, "0:-1 empty");
    
    x = 0:0;
    check(x.size == 0, "0:0 size");
    check(!x nonempty, "0:0 empty");
    
    x = 0:1;
    check(x.size == 1, "0:1 size");
    check(x nonempty, "0:1 nonempty");
    check(x.string == "0..0", "0:1 string == " + x.string);
    
    x = 0:10;
    check(x.size == 10, "0:10 size");
    check(x nonempty, "0:10 nonempty");
    check(x.string == "0..9", "0:1 string == " + x.string);
    
    check((0..20).segment(0, 10) == 0:10, "segment method & segment op");
    check((0..20)[0:10] == 0:10, "segment index & segment op");
    
    variable value az = 'a':26;
    check(az.string == "a..z", "a:26 string == " + az.string);
    
    check((1..5).by(2).sequence==[1,3,5], "int range by 1");
    check((1..4).by(2).sequence==[1,3], "int range by 2");
    check(('a'..'e').by(2).sequence==['a','c','e'], "char range by 1");
    check((1..4).shifted(2)==3..6, "int range shift 1");
    check((1..5).shifted(-2)==-1..3, "int range shift 2");
    check(('a'..'c').shifted(1)=='b'..'d', "char range shift 1");
    
    check((1..3).longerThan(2), "range longerThan");
    check(!(1..3).longerThan(3), "range not longerThan");
    check(!(1..3).shorterThan(3), "range not shorterThan");
    check((1..3).shorterThan(4), "range shorterThan");
}