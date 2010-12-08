void test(Process p) {
    String a = "Hello";
    String b = "Hello";
    
    if (a == b) {
        p.writeLine("pass");
    } else {
        p.writeLine("fail");
    }        
    
    if (a != b) {
        p.writeLine("fail");
    } else {
        p.writeLine("pass");
    }    

    mutable Integer c := 0;

    while (c != 2) {
        c := c + 1;
    }
    if (c == 2) {
        p.writeLine("pass");
    } else {
        p.writeLine("fail");
    }

    c := 0;
    while (! c == 3) {
        c++;
    }
    if (c == 3) {
        p.writeLine("pass");
    } else {
        p.writeLine("fail");
    }
}