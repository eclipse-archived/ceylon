shared abstract class FunctionalParameterParameterNames() {
    shared formal void functionalParameter(void f(String s));
    
    shared formal void callableValueParameter(Anything(String) f);
    
    shared formal void functionalParameterNested(void f(void f2(String s)));
    shared formal void functionalParameterNested2(void f(void f2(String s, void f3(Boolean b1, Integer i2))));
    
    shared formal void functionalParameterMpl(void mpl(String s)(Integer i2));
    shared formal void functionalParameterMpl2(void mpl(String s)(void f(Boolean b1, Integer i2)));
    shared formal void functionalParameterMpl3(void mpl(void f(Boolean b1, Integer i2))(String s));
    
    shared formal void functionalParameterReturningCallable(Anything() f(String s));
    shared formal void functionalParameterTakingCallable(void f(Anything(String) f2));
    
    shared formal void mpl(String s)(Integer i);
    
    shared formal void functionalParameterVariadicStar(void f(String* s));
    shared formal void functionalParameterVariadicPlus(void f(String+ s));
}