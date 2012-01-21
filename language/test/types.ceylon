class T() extends Object() {
    shared actual String string = "hello";
}

interface Format {}

void types() {
    Void bool = true;
    Void entry = 1->2;
    Void nothing = null;
    Void one = 1;
    Void t = T();
    Void c = `c`;
    Void str = "string";
    //Void seq = {"hello"};
    //Void empty = {};
    
    assert(is Object bool, "boolean type is object");
    assert(is IdentifiableObject bool, "boolean type is identifiable");
    assert(is Equality bool, "boolean type is equality");
    assert(!is Nothing bool, "not null boolean type is not nothing");
    assert(is Boolean bool, "boolean type");
    assert(is Void bool, "boolean type");
    //assert(is Nothing|Boolean bool, "boolean type");
    
    assert(is Nothing nothing, "null type");
    assert(!is Equality nothing, "null type");
    assert(!is Object nothing, "null type");
    assert(!is IdentifiableObject nothing, "null type");
    assert(is Void nothing, "null type");
        
    assert(is Object entry, "entry type");
    assert(!is IdentifiableObject entry, "not entry type");
    assert(is Equality entry, "entry type");
    assert(!is Nothing entry, "not null entry type");
    //assert(is Entry<Integer,Integer> entry, "entry type");
    assert(is Void entry, "entry type");
        
    assert(is Object one, "not natural type");
    assert(!is IdentifiableObject one, "not natural type");
    assert(is Equality one, "natural type");
    assert(!is Nothing one, "not null natural type");
    assert(is Integer one, "natural type");
    assert(is Void nothing, "natural type");
        
    assert(is Object c, "not char type");
    assert(!is IdentifiableObject c, "not char type");
    assert(is Equality c, "char type");
    assert(!is Nothing c, "not null char type");
    assert(is Character c, "char type");
    assert(is Void c, "char type");
        
    assert(is Object str, "not string type");
    assert(!is IdentifiableObject str, "not string type");
    assert(is Equality str, "string type");
    assert(!is Nothing str, "not null string type");
    assert(is String str, "string type");
    assert(is Void str, "string type");
            
    assert(!is Equality t, "not eq custom type");
    assert(!is IdentifiableObject t, "not id custom type");
    assert(!is Nothing t, "custom type");
    assert(is Object t, "custom type");
    assert(is T t, "custom type");
    assert(is Void t, "custom type");
                
    if (is Equality bool) {} else { fail("boolean type"); }
    if (is IdentifiableObject bool) {} else { fail("boolean type"); }
    if (is Object bool) {} else { fail("boolean type"); }
    if (is Nothing bool) { fail("null type"); }
    if (is Boolean? bool) {} else { fail("optional boolean type"); }

    if (is Equality one) {} else { fail("natural type"); }
    if (is IdentifiableObject one) { fail("natural type"); }
    if (is Object one) {} else { fail("natural type"); }
    if (is Nothing one) { fail("null type"); }
    if (is Integer one) {} else { fail("natural type"); }
    if (is Integer? one) {} else { fail("optional natural type"); }

    if (is Equality c) {} else { fail("character type"); }
    if (is IdentifiableObject c) { fail("character type"); }
    if (is Object c) {} else { fail("character type"); }
    if (is Nothing c) { fail("null type"); }
    if (is Character c) {} else { fail("character type"); }
    if (is Character? c) {} else { fail("optional character type"); }

    if (is Equality str) {} else { fail("string type"); }
    if (is IdentifiableObject str) { fail("string type"); }
    if (is Object str) {} else { fail("string type"); }
    if (is Nothing str) { fail("null type"); }
    if (is String? str) {} else { fail("optional string type"); }

    if (is Equality t) { fail("custom type"); }
    if (is IdentifiableObject t) { fail("custom type"); }
    if (is Object t) {} else { fail("custom type"); }
    if (is Nothing t) { fail("null type"); }
    if (is T? t) {} else { fail("optional custom type"); }

    if (is Equality entry) {} else { fail("entry type"); }
    if (is IdentifiableObject entry) { fail("entry type"); }
    if (is Object entry) {} else { fail("entry type"); }
    if (is Nothing entry) { fail("null type"); }
    //if (is Entry<Integer,Integer> entry) {} else { fail("entry type"); }
    
    if (is Equality nothing) { fail("null type"); }
    if (is IdentifiableObject nothing) { fail("null type"); }
    if (is Object nothing) { fail("null type"); }
    if (is Nothing nothing) {} else { fail("null type"); }
    if (is Character? nothing) {} else { fail("null is optional type"); }
    
    if (is Boolean|Character|T bool) {} else { fail("union type"); }
    if (is Boolean|Character|T t) {} else { fail("union type"); }
    if (is Boolean|Character|T str) { fail("union type"); } else {}
    if (is Boolean|Character|T nothing) { fail("union type"); } else {}
    if (is Equality&Castable<Bottom> one) {} else { fail("intersection type"); }
    if (is Equality&Castable<Bottom> bool) { fail("intersection type"); } else {}
    if (is Sized&Category&Iterable<Void> str) {} else { fail("intersection type"); }
    if (is Sized&Category&Iterable<Void> t) { fail("intersection type"); } else {}
    //if (is String[] empty) {} else { fail("sequence type"); }
    //if (is String[] seq) {} else { fail("sequence type"); }
    //if (is String[]? seq) {} else { fail("sequence type"); }
    //if (is Integer[] seq) { fail("sequence type"); } else {}
    
    assert(className(1)=="ceylon.language.Integer", "natural classname");
    assert(className(1.0)=="ceylon.language.Float", "float classname");
    assert(className("hello")=="ceylon.language.String", "string classname");
    assert(className(1->"hello")=="ceylon.language.Entry", "entry classname");
}