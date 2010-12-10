// This test fails because == does not work on optional types.

void test(Process p) {
    String? d = null;
    String? e = null;

    if (d == e) {
        p.writeLine("pass");
    } else {
        p.writeLine("fail");
    }

    if (d == a) {
        p.writeLine("fail");
    } else {
        p.writeLine("pass");
    }   
}
