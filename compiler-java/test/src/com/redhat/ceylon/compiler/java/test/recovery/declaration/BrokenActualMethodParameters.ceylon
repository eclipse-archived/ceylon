class BrokenActualMethodParameters<A>()
        satisfies FormalMethods<A,String> {
    
    shared actual String simpleMethodNullary(Integer i)=> "";// extra parameter
    shared actual A tpMethodANullary(Integer i) => nothing;// extra parameter
    shared actual String tpMethodBNullary(Integer i) => "";// extra parameter
    shared actual C tpMethodCNullary<C>(Integer i) => nothing;// extra parameter
    
    
    shared actual void simpleMethodUnary() {}// missing parameter
    shared formal void sequencedMethodUnary(String+ arg);// wrong multiplicity
    shared formal void nonEmptySequencedMethodUnary(String arg);//should be sequenced
    shared actual void tpMethodAUnary{}// missing parameter list
    shared actual void tpMethodBUnary(Float f){}// wrong parameter type
    shared actual void tpMethodCUnary<C>(C x){}//TODO nothing wrong with this right now
    
    shared actual void methodMpl<C>(A a)(String b){}// too few parameter lists
    
    shared actual void simpleMethodFunctional(void functional(Float a)){}// wrong type in functional parameter
    shared actual void tpMethodFunctionalA(String functional()){}// too few in functional parameter
    /*shared actual void tpMethodFunctionalB(A functional(B a));
    shared actual void tpMethodFunctionalC<C>(B functional(C a));*/
    
    
}
object brokenActualMethodParameters
        satisfies FormalMethods<Integer,String> {
    shared actual String simpleMethodNullary(Integer extraParameter)=> "";
    shared actual Integer tpMethodANullary(Integer extraParameter) => nothing;
    shared actual String tpMethodBNullary(Integer extraParameter) => "";
    shared actual C tpMethodCNullary<C>(Integer extraParameter) => nothing;
    
    
    shared actual void simpleMethodUnary() {}// missing parameter
    shared actual void tpMethodAUnary{}// missing parameter list
    shared actual void tpMethodBUnary(Float f){}// wrong parameter type
    shared actual void tpMethodCUnary<C>(C x){}//TODO nothing wrong with this right now
    
    shared actual void methodMpl<C>(Integer a)(String b){}// too few parameter lists
    
    shared actual void simpleMethodFunctional(void functional(Float a)){}// wrong type in functional parameter
    shared actual void tpMethodFunctionalA(String functional()){}// too few in functional parameter
    /*shared actual void tpMethodFunctionalB(A functional(B a));
    shared actual void tpMethodFunctionalC<C>(B functional(C a));*/
}