interface NamedArgumentInvocation {
    shared default Integer f(Integer i) {
        return i;
    }
}
interface NamedArgumentInvocationSub satisfies NamedArgumentInvocation {
    Integer g(Integer i=1) => i;
    
    shared actual Integer f(Integer i) {
        
        g{};
        this.g{};
        
        super.f{
            i=i;
        };
        super.f{
            
        };
        return 1;
    }
}