@nomodel
class NamedArgumentInvocationInit(String a, Natural b) {
    class Inner(Boolean x, String y, Integer z) {
    	shared void m2() { }
    }
    void m() {
    	NamedArgumentInvocationInit{
    	    b=13;
    	    a="a";
    	}.m();
    	Inner {
    	    y="";
    	    z=-4;
    	    x=true;
    	}.m2();
    }
}