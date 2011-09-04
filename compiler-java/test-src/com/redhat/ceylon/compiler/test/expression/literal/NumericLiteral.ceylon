@nomodel
class NumericLiteral(){
    shared void m() {
    	// make sure we test all the max/min limits
        Natural n1 = 9223372036854775807;
        Integer n2 = +9223372036854775807;
        Integer n3 = -9223372036854775808;
        Float n4 = 3.14159;
    }
}