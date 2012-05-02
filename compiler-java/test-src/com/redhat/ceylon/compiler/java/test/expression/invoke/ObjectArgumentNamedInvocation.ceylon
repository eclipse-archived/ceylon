@nomodel
shared void objectArgumentNamedInvocation() {

    void callFunction(Object o) {
    }
    
    String o = "";// test alias

    callFunction {
        object o extends Object() {
        }
    };
}