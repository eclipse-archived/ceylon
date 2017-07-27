final annotation class Bug7100Foo(String* names) 
        satisfies OptionalAnnotation<Bug7100Foo> {}

annotation Bug7100Foo bug7100_foo(String* names) => Bug7100Foo("thing", *names);

bug7100_foo("hello", "world")
shared void bug7100() {}
