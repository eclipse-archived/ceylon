@nomodel
class F() satisfies Format {
	shared actual String formatted = "F";
}
@nomodel
class StringTemplate() {
    void m1(String s) {
        String t = "Foo " s " bar";
    }
    void m2(String s) {
        String t = "Foo " s "";
    }
    void m3(Natural n) {
    	String t = "Foo " n " bar";
    }
    /*void m4() {
        String t = "Foo " ("") " bar";
    }*/
    void m5() {
    	String t = "Foo " 4 " bar";
    }
    void m6(F f) {
        String t = "Foo " f " bar";
    }
}