// Test we can use the contrains in the scope of the type constructor 
// type parameter
class ParameterizedTypeParameterWithConstraints<T, U, V>()
            given T<X> satisfies X()
            given U<Y> satisfies Anything(Y)
            given V<Z> satisfies List<Z>
{
    T<String> attribute = nothing;
    String s = attribute();
    U<String> attribute2 = nothing;
    Anything s2 = attribute2("");
    V<String> attribute3 = nothing;
    String? s3 = attribute3[0];
}

shared interface ParameterizedTypeParameter<in Container, out Container2> 
        given Container<Element> 
        given Container2<Element> {
    shared formal void acceptInstantiateTp<Element>(Container<Element> instance);
    shared formal void acceptInstantiateClass(Container<String> instance);
    shared formal Container2<Element> returnInstantiateTp<Element>();
    shared formal Container2<String> returnInstantiateClass();
}

shared class ParameterizedTypeParameterSatisfier<X>() 
        satisfies ParameterizedTypeParameter<List, List> 
        given X<Y> given Y satisfies Object {
    
    shared actual void acceptInstantiateTp<Element2>(List<Element2> instance) {}
    shared actual void acceptInstantiateClass(List<String> instance) {}
    shared actual List<Element> returnInstantiateTp<Element>() => nothing;
    shared actual List<String> returnInstantiateClass() => nothing;
    
    shared default void acceptInstantiateTp2<T>(X<T> x) {}
    shared default void acceptInstantiateClass2(X<String> x) {}
    shared default X<T> returnInstantiateTp2<T>() => nothing;
    shared default X<String> returnInstantiateClass2() => nothing;
    
    
}
shared class ParameterizedTypeParameterExtender() 
        extends ParameterizedTypeParameterSatisfier<Set>() {
    shared actual void acceptInstantiateTp2<T>(Set<T> x) {}
    shared actual void acceptInstantiateClass2(Set<String> x) {}
    shared actual Set<U> returnInstantiateTp2<U>() => nothing;
    shared actual Set<String> returnInstantiateClass2() => nothing;
}

//alias Alias=>String|ParameterizedTypeParameter<<X>=>Anything, <Y>=>Anything>;
