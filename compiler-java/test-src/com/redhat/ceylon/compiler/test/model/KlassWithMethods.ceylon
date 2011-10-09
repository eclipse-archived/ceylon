abstract class Super2() {
 shared formal void formalMethod();
 shared formal void formalMethod2();
 shared default void defaultMethod(){}
}

abstract class Super1() extends Super2() {
 // we implement a formal method
 shared actual void formalMethod(){}
 // we give a default impl to a formal method
 shared actual default void formalMethod2(){}
 // we make a default method formal
 shared actual formal void defaultMethod();
}

class KlassWithMethods() extends Super1() {
 void empty(){}
 shared void emptyPublic(){}
 Natural natural(){return 1;}
 shared Natural naturalPublic(){return 1;}
 Natural param(Natural p){return p;}
 shared Natural paramPublic(Natural p){return p;}
 // override all formal methods
 shared actual void formalMethod2(){}
 shared actual void defaultMethod(){}
 
 // varargs
 shared void varargs(Natural... args){}
}
