
@test
shared void testContextual() {
    Boolean eq(Anything a, Anything b)
        =>  if (exists a, exists b)
            then a == b
            else a exists == b exists;

    Contextual<String> stringValue = Contextual<String>();
    Contextual<String?> optStringValue = Contextual<String?>();
    Contextual<Integer> intValue = Contextual<Integer>();
    try (stringValue.Using("a"), intValue.Using(1)) {
        check(stringValue.get()=="a", "contextual string");
        check(intValue.get()==1, "contextual integer before");
        try (intValue.Using(2)) {
            check(intValue.get()==2, "contextual integer nested");
        }
        check(intValue.get()==1, "contextual integer after");
    }
    try (optStringValue.Using(null)) {
        check(!optStringValue.get() exists, "contextual optional string null #1");
    }
    // Note: Contextuals can't distinguish between null and not-set!
    check(!optStringValue.get() exists, "contextual optional string null #2");

    check(eq(stringValue.getOrElse("other"), "other"), "contextual getOrElse #1");
    check(eq(optStringValue.getOrElse("other"), "other"), "contextual getOrElse #2");

    check(!stringValue.getOrElse(null) exists, "contextual getOrElse #3");
    check(!optStringValue.getOrElse(null) exists, "contextual getOrElse #4");
}

