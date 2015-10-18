
@test
shared void testContextual() {
    Contextual<String> stringValue = Contextual<String>();
    Contextual<Integer> intValue = Contextual<Integer>();
    try (stringValue.Using("a"), intValue.Using(1)) {
        check(stringValue.get()=="a", "contextual string");
        check(intValue.get()==1, "contextual integer before");
        try (intValue.Using(2)) {
            check(intValue.get()==2, "contextual integer nested");
        }
        check(intValue.get()==1, "contextual integer after");
    }
}
