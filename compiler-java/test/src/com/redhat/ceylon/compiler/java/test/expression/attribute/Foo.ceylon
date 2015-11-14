@noanno
class Foo(init, sharedInit, String param, shared String sharedParam) {
    String init;
    shared String sharedInit;
    
    String s3 = init + sharedInit + param + sharedParam;
    shared void m(String a = init + sharedInit + param + sharedParam) {
        
    }
}
@noanno
class Bar(init, variableInit){
    String? init;
    variable String? variableInit;
    variable String? variableAttr = variableInit;
    String? attr = variableInit;
    shared void m() {
        value x1 = init;
        value x2 = attr;
        value x3 = variableAttr;
        value x4 = variableInit;
        variableAttr = "";
        variableInit = "";
    }
}