shared interface Top<out A>{
    shared formal A inheritedMethod();
    shared formal A inheritedAttribute;
    shared class InheritedClass(){}
    shared interface InheritedInterface{}
}
shared interface Middle<out A> satisfies Top<A>{
}
shared abstract class MiddleClass<out A>() satisfies Middle<A>{}

shared abstract class BottomClass() extends MiddleClass<Object>() satisfies Middle<String>{
    String privateAttribute = "";
    void privateMethod(){}
    shared formal String declaredMethod(String s);
    shared formal String declaredAttribute;
    shared class DeclaredClass(){}
    shared interface DeclaredInterface{}
    shared void myOwnBottomMethod(){}
}