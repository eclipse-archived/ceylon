@nomodel
class NamedArgumentInvocationInitWithSequence(String a, Natural... b) {
    class Inner(Boolean x, String y, Integer... z) {
    	shared void m2() { }
    }
    void m() {
    	NamedArgumentInvocationInitWithSequence{
    	    a="a";
    	    1, 2, 3
    	}.m();
    	Inner {
    	    y="";
    	    x=true;
    	    +4, +5, +6
    	}.m2();
    }
}