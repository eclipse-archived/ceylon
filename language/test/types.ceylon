class T() extends Object() {
    shared actual String string = "hello";
}

void types() {
    Object bool = true;
    Object entry = 1->2;
    Void nothing = null;
    Object one = 1;
    Object t = T();
    assert(is Object bool, "boolean type");
    assert(is IdentifiableObject bool, "boolean type");
    assert(is Equality bool, "boolean type");
    assert(!is Nothing bool, "not null boolean type");
    
    assert(is Nothing nothing, "null type");
    assert(!is Equality nothing, "null type");
    assert(!is Object nothing, "null type");
    assert(!is IdentifiableObject nothing, "null type");
    
    assert(is Object entry, "entry type");
    assert(!is IdentifiableObject entry, "not entry type");
    assert(is Equality entry, "entry type");
    assert(!is Nothing entry, "not null entry type");
    
    assert(is Object one, "not natural type");
    assert(!is IdentifiableObject one, "not natural type");
    assert(is Equality one, "natural type");
    assert(!is Nothing one, "not null natural type");
    
    assert(!is Equality t, "not eq custom type");
    assert(!is IdentifiableObject t, "not id custom type");
    assert(!is Nothing t, "custom type");
    assert(is Object t, "custom type");
    
    if (is Equality t) { fail("custom type"); }
    if (is IdentifiableObject t) { fail("custom type"); }
    if (is Object t) {} else { fail("custom type"); }

    if (is Equality entry) {} else { fail("entry type"); }
    if (is IdentifiableObject entry) { fail("entry type"); }
    if (is Object entry) {} else { fail("entry type"); }
    
    assert(className(1)=="ceylon.language.Natural", "natural classname");
    assert(className(1.0)=="ceylon.language.Float", "float classname");
    assert(className("hello")=="ceylon.language.String", "string classname");
    assert(className(1->"hello")=="ceylon.language.Entry", "entry classname");
}