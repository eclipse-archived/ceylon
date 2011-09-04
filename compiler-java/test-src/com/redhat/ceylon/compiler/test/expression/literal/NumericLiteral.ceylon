@nomodel
class NumericLiteral(){
    shared void m() {
    	// make sure we test all the max/min limits
        Natural n1 = 9223372036854775807;
        Integer n2 = +2147483647;
        Integer n3 = -2147483648;
        Float n4 = 1.7976931348623157E308;
        Float n5 = 4.9E-324;
    }
}