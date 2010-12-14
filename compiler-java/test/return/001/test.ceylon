void test (Process p) {
 
    void nothing() {
	return;
    }

    String result() {
	return "OK";
    }
    
    p.writeLine(result());
}
