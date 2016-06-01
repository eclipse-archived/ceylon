@noanno
interface Super {
    shared actual String string => super.string;
}
@noanno
interface SuperEmpty {
    
}

@noanno
interface SuperSatisfiesSuperEmpty satisfies SuperEmpty {
    shared actual String string => super.string;
}

@noanno
interface SuperIdentifiable satisfies Identifiable {
    shared actual default String string => super.string;
    shared actual default Integer hash => super.hash;
    shared actual default Boolean equals(Object other) => super.equals(other);
}

@noanno
class SuperClassNoExtends() satisfies SuperEmpty {
    shared actual String string => super.string;
    shared actual Integer hash => super.hash;
    shared actual Boolean equals(Object other) => super.equals(other);
}
@noanno
abstract class SuperClassExtendsObject() extends Object() satisfies SuperEmpty {
    shared actual String string => super.string;
}
@noanno
class SuperClassExtendsBasic() extends Basic() satisfies SuperEmpty {
    shared actual String string => super.string;
    shared actual Integer hash => super.hash;
    shared actual Boolean equals(Object other) => super.equals(other);
}
@noanno
class SuperIdentifiableClassNoExtends() satisfies SuperIdentifiable {
    shared actual String string => super.string;
    shared actual Integer hash => super.hash;
    shared actual Boolean equals(Object other) => super.equals(other);
}
@noanno
class SuperIdentifiableExtendsObject() extends Object() satisfies SuperIdentifiable {
    shared actual String string => super.string;
    shared actual Integer hash => super.hash;
    shared actual Boolean equals(Object other) => super.equals(other);
}
@noanno
class SuperIdentifiableExtendsBasic() extends Basic() satisfies SuperIdentifiable {
    shared actual String string => super.string;
    shared actual Integer hash => super.hash;
    shared actual Boolean equals(Object other) => super.equals(other);
}
@compileUsing:"companion"
shared interface AttributeValueProvider {
    
    "The attribute value."
    shared formal String? attributeValue;
    
    string => attributeValue else super.string;
    
}