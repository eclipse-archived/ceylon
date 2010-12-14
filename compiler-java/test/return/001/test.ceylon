void test (Process p) {
 
    void nothing() {
	return;
    }

    String result() {
	return "OK";
    }

    String testExtends() {
	return 99;
    }
   
    Integer testExtends1() {
	return 99;
    }
   
    p.writeLine(result());
    p.writeLine(testExtends());
    p.writeLine(testExtends1());
}
