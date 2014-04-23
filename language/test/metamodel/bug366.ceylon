import ceylon.language.meta.declaration { ClassDeclaration }
import ceylon.language.meta.model { IncompatibleTypeException, InvocationException, Function, Class, Applicable}

Boolean bug366f<T>(T a, Integer b = 2, Object o = 3)
    given T satisfies Object {
    
    assert(a == 1);
    assert(b == 2);
    assert(o == 3);
    return true;
}

class Bug366c<T>(T a, Integer b = 2, Object o = 3)
    given T satisfies Object {
    
    assert(a == 1);
    assert(b == 2);
    assert(o == 3);
    
    shared Boolean f<T>(T a, Integer b = 2, Object o = 3)
        given T satisfies Object {
        
        assert(a == 1);
        assert(b == 2);
        assert(o == 3);
        return true;
    }

    shared class Inner<T>(T a, Integer b = 2, Object o = 3)
        given T satisfies Object {
        
        assert(a == 1);
        assert(b == 2);
        assert(o == 3);
    }
}

@test
shared void bug366() {
    // toplevel function
    value toplevelFunction = `bug366f<Integer>`;
    bug366Check(toplevelFunction);
    
    // method
    value instance = Bug366c<Integer>(1);
    value method = `Bug366c<Integer>.f<Integer>`(instance);

    bug366Check(method);

    // toplevel class
    value toplevelClass = `Bug366c<Integer>`;
    bug366Check(toplevelClass);

    // member class
    value memberClass = `Bug366c<Integer>.Inner<Integer>`(instance);
    
    bug366Check(memberClass);
}

void bug366Check(Applicable<Anything> f){
    assert(f.namedApply{ "a" -> 1 } exists);
    assert(f.namedApply{ "a" -> 1, "b" -> 2 } exists);
    assert(f.namedApply{ "a" -> 1, "b" -> 2, "o" -> 3 } exists);
    assert(f.namedApply{ "a" -> 1, "o" -> 3 } exists);
    
    try{
        f.namedApply{ "a" -> "f" };
        assert(false);
    }catch(IncompatibleTypeException x){
        // good!
    }
    
    try{
        f.namedApply{};
        assert(false);
    }catch(InvocationException x){
        // good!
    }
    
    try{
        f.namedApply{ "a" -> 1, "nonexistent" -> 2 };
        assert(false);
    }catch(InvocationException x){
        // good!
    }
}
