@noanno
class Bug1155_B() {
    
    // Used for anonymous functions and method arguments
    shared void f(Anything(Nothing) fn) {}
    
    shared void binaryStar(String s, String* seq) {
    }
    shared void unaryPlus(String+ seq) {
    }
    shared void binaryOptStar(String s="", String* seq) {
    }
    
    shared void mva_callsite() {
        Anything(String, String*) binaryStarRef = binaryStar;
        f(void(String s, String* seq) {});
        Anything(String+) unaryPlusRef = unaryPlus;
        Anything(String=, String*) binaryOptStarRef = binaryOptStar;
        // Illegal: Anything(String+) x = binaryStar;
        // Legal:   Anything(String, String*) y = unaryPlus;
    }
}