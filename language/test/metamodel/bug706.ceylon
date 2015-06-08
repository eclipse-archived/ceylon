import ceylon.language.meta.model {...}

@test
shared void bug706() {
    by("me") class Foo() {}
    print(`class Foo`.annotated<Annotation>());
}
