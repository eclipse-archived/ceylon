import ceylon.language.meta.model { ... }

class Bug284(){
    shared class Inner(String x){}
    shared String method(Integer i){ return ""; }
    shared Integer attr = 2;
}

void bug284() {
    value b = `Bug284`;
    assert(is MemberClass<Bug284, Bug284.Inner, [String]> ic = b.getClassOrInterface<Nothing,ClassOrInterface<Anything>>("Inner"));
    assert(exists m = b.getMethod<Nothing,Anything,Nothing>("method"));
    assert(is Method<Bug284, String, [Integer]> m);
    assert(is Attribute<Bug284, Integer> a = b.getAttribute<Nothing,Anything>("attr"));
}