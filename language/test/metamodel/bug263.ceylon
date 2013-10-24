final annotation class Bug263Annotation(shared actual String string) 
        satisfies OptionalAnnotation<Bug263Annotation> {}

annotation Bug263Annotation bug263annotation(String s) => Bug263Annotation(s);

@test
bug263annotation("hello")
shared void bug263() {
    print(`function bug263`.annotations<Bug263Annotation>());
}