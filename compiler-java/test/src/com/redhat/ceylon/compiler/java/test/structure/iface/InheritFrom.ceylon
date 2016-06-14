interface InheritFrom {
    shared actual String string => "InheritFrom";
    //shared actual Boolean equals(Object other) => true;
    //shared actual Integer hash => 42;
}
class InheritFromBasic() {}
class InheritFromBasicIndirectly() extends InheritFromBasic() satisfies InheritFrom {
    
}

interface InheritFrom2 {
    shared actual String string => "InheritFrom2";
    shared actual Boolean equals(Object other) => true;
    shared actual Integer hash => 42;
}
class InheritFromObject() extends Object() satisfies InheritFrom2 {
    
}

interface InheritFrom3 satisfies Identifiable {
    shared actual String string => "InheritFrom3";
    shared actual Boolean equals(Object other) => true;
    shared actual Integer hash => 42;
}
class InheritFromBasicWithIdentifiableClass() /*extends Basic()*/ satisfies InheritFrom3 {
    
}

interface Itbl {
    shared actual default String string => "wtf";
    shared actual default Boolean equals(Object other) { return true; }
}

interface L satisfies Itbl {
    shared actual default String string => "wtf2";
    shared actual default Boolean equals(Object other) { return true; }
}

interface Sql satisfies L {
}
interface Sqc satisfies Sql & Itbl {
}
class As() extends Object() satisfies Sqc {
    shared actual Integer hash => 0;
}

void inheritFrom() {
    assert("InheritFrom" == InheritFromBasicIndirectly().string);
    
    assert("InheritFrom2" == InheritFromObject().string);
    assert(InheritFromObject().equals(1));
    assert(42 == InheritFromObject().hash);
    
    assert("InheritFrom3" == InheritFromBasicWithIdentifiableClass().string);
    assert(InheritFromBasicWithIdentifiableClass().equals(1));
    assert(42 == InheritFromBasicWithIdentifiableClass().hash);
    print(As());
    assert(As()==As());
}