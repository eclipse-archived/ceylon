void testRange() {
    // TODO RangeOp
    
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
    
}