class BadActualMethodParameters<A>()
        extends CFormalClasses<A,String>()
        satisfies IFormalClasses<A,String> {
    
    shared actual class ISimpleMethodNullary(Integer i){}//extra parameter
    shared actual class ITpMethodANullary(Integer i) {}//extra parameter
    shared actual class ITpMethodBNullary(Integer i) {}//extra parameter
    shared actual class ITpMethodCNullary<C>(Integer i) {}//extra parameter
    
    shared actual class ISimpleMethodUnary() {}// missing parameter
    shared actual class ITpMethodAUnary{}// missing parameter list
    shared actual class ITpMethodBUnary(Float f){}// wrong parameter type
    shared actual class ITpMethodCUnary<C>(Missing m){}// missing type in parameter list
    
    //shared actual class IMethodMpl<C>(A a)(String b){}// too few parameter lists
    
    shared actual class ISimpleMethodFunctional(void functional(Float a)){}// wrong type in functional parameter
    shared actual class ITpMethodFunctionalA(String functional()){}// too few in functional parameter
    shared actual class ITpMethodFunctionalB(A functional(B a));//unknown type in functional parameter list
    shared actual class ITpMethodFunctionalC<C>(B functional(C a));
    
}