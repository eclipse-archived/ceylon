@nomodel
shared void objectArgumentNamedInvocation() {

    void callFunction(Object o) {
    }
    
    String o = "";// test alias

    callFunction {
        object o extends Object() {
            shared actual Boolean equals(Object other) {return false;}
            shared actual Integer hash = 0;
        }
    };
}