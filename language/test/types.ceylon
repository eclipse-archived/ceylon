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
    assert(is Boolean bool, "boolean type 1");
    assert(is Void bool, "boolean type 2");
    //assert(is Nothing|Boolean bool, "boolean type 3");
    
    assert(is Nothing nothing, "null type 1");
    assert(!is Equality nothing, "null type 2");
    assert(!is Object nothing, "null type 3");
    assert(!is IdentifiableObject nothing, "null type 4");
    assert(is Void nothing, "null type 5");
        
    assert(is Object entry, "entry type 1");
    assert(!is IdentifiableObject entry, "not entry type");
    assert(is Equality entry, "entry type 2");
    assert(!is Nothing entry, "not null entry type");
    //assert(is Entry<Integer,Integer> entry, "entry type 3");
    assert(is Void entry, "entry type 4");
        
    assert(is Object one, "not natural type 1");
    assert(!is IdentifiableObject one, "not natural type 2");
    assert(is Equality one, "natural type 1");
    assert(!is Nothing one, "not null natural type");
    assert(is Integer one, "natural type 2");
    assert(is Void nothing, "natural type 3");
        
    assert(is Object c, "not char type 1");
    assert(!is IdentifiableObject c, "not char type 1");
    assert(is Equality c, "char type 1");
    assert(!is Nothing c, "not null char type");
    assert(is Character c, "char type 2");
    assert(is Void c, "char type 3");
        
    assert(is Object str, "not string type 1");
    assert(!is IdentifiableObject str, "not string type 1");
    assert(is Equality str, "string type 1");
    assert(!is Nothing str, "not null string type");
    assert(is String str, "string type 2");
    assert(is Void str, "string type 3");
            
    assert(!is Equality t, "not eq custom type");
    assert(!is IdentifiableObject t, "not id custom type");
    assert(!is Nothing t, "custom type 1");
    assert(is Object t, "custom type 2");
    assert(is T t, "custom type 3");
    assert(is Void t, "custom type 4");
                
    if (is Equality bool) {} else { fail("boolean type 4"); }
    if (is IdentifiableObject bool) {} else { fail("boolean type 5"); }
    if (is Object bool) {} else { fail("boolean type 6"); }
    if (is Nothing bool) { fail("null type 6"); }
    if (is Boolean? bool) {} else { fail("optional boolean type 7"); }

    if (is Equality one) {} else { fail("natural type 4"); }
    if (is IdentifiableObject one) { fail("natural type 5"); }
    if (is Object one) {} else { fail("natural type 6"); }
    if (is Nothing one) { fail("null type 7"); }
    if (is Integer one) {} else { fail("natural type 7"); }
    if (is Integer? one) {} else { fail("optional natural type 8"); }

    if (is Equality c) {} else { fail("character type 1"); }
    if (is IdentifiableObject c) { fail("character type 2"); }
    if (is Object c) {} else { fail("character type 3"); }
    if (is Nothing c) { fail("null type 8"); }
    if (is Character c) {} else { fail("character type 4"); }
    if (is Character? c) {} else { fail("optional character type 5"); }

    if (is Equality str) {} else { fail("string type 4"); }
    if (is IdentifiableObject str) { fail("string type 5"); }
    if (is Object str) {} else { fail("string type 6"); }
    if (is Nothing str) { fail("null type 9"); }
    if (is String? str) {} else { fail("optional string type 7"); }

    if (is Equality t) { fail("custom type 5"); }
    if (is IdentifiableObject t) { fail("custom type 6"); }
    if (is Object t) {} else { fail("custom type 7"); }
    if (is Nothing t) { fail("null type 10"); }
    if (is T? t) {} else { fail("optional custom type 8"); }

    if (is Equality entry) {} else { fail("entry type 5"); }
    if (is IdentifiableObject entry) { fail("entry type 6"); }
    if (is Object entry) {} else { fail("entry type 7"); }
    if (is Nothing entry) { fail("null type 11"); }
    //if (is Entry<Integer,Integer> entry) {} else { fail("entry type 8"); }
    
    if (is Equality nothing) { fail("null type 12"); }
    if (is IdentifiableObject nothing) { fail("null type 13"); }
    if (is Object nothing) { fail("null type 14"); }
    if (is Nothing nothing) {} else { fail("null type 15"); }
    if (is Character? nothing) {} else { fail("null is optional type"); }
    
    if (is Boolean|Character|T bool) {} else { fail("union type"); }
    if (is Boolean|Character|T t) {} else { fail("union type"); }
    if (is Boolean|Character|T str) { fail("union type"); } else {}
    if (is Boolean|Character|T nothing) { fail("union type"); } else {}
    if (is Equality&Castable<Bottom> one) {} else { fail("intersection type"); }
    if (is Equality&Castable<Bottom> bool) { fail("intersection type"); } else {}
    if (is Sized&Category&Ordered<Void> str) {} else { fail("intersection type"); }
    if (is Sized&Category&Ordered<Void> t) { fail("intersection type"); } else {}
    //if (is String[] empty) {} else { fail("sequence type"); }
    //if (is String[] seq) {} else { fail("sequence type"); }
    //if (is String[]? seq) {} else { fail("sequence type"); }
    //if (is Integer[] seq) { fail("sequence type"); } else {}
    
    assert(className(1)=="ceylon.language.Integer", "natural classname");
    assert(className(1.0)=="ceylon.language.Float", "float classname");
    assert(className("hello")=="ceylon.language.String", "string classname");
    assert(className(1->"hello")=="ceylon.language.Entry", "entry classname");
}
