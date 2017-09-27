import ceylon.language.meta.model { ... }
import ceylon.language.meta.declaration { ... }

class Bug6897 {
    shared class BB {
        shared new create() {}
        shared new instance {}
    }
    shared new () {}
    shared new create() {}
    shared new instance {}
}

shared void bug6897() {

    assert (is CallableConstructor<Anything,[]> callableCtor0
        =   `Bug6897`.getConstructor<[]>("create"));
    
    assert (is ValueConstructor<Anything> valueCtor0
        =   `Bug6897`.getConstructor("instance"));
    
    print(callableCtor0());
    print(valueCtor0.get());

    
    // obtain a usable constructor from a bound member class
    assert (is CallableConstructor<Anything,[]> callableCtor
        =   `Bug6897.BB`(Bug6897()).getConstructor<[]>("create"));
    
    print(callableCtor());
    
    assert (is MemberClassCallableConstructor<Bug6897,Bug6897.BB,[]> callableCtor2
        =   `Bug6897.BB`.getConstructor<[]>("create"));
    
    print(callableCtor2(Bug6897())());

    assert (is ValueConstructor<Anything> valueCtor
        =   `Bug6897.BB`(Bug6897()).getConstructor("instance"));
    
    print(valueCtor.get());
    
    assert (is MemberClassValueConstructor<Bug6897, Object> valueCtor2
        =   `Bug6897.BB`.getConstructor("instance"));
    
    print(valueCtor2(Bug6897()).get());
    

}