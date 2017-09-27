@noanno
class Bug5741() {
    Anything m() {
        variable value n = 1;
        // 1) capture some variable in a method:
        return () => n;
    }
    
    // 2) make an assertion on an attribute
    Anything s = [];
    assert(is {Anything*} s);
    
    // 3) capture the asserted attribute
    shared Anything t => s;
}
