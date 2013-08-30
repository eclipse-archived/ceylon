import ceylon.language.model { Annotation }

final annotation class Bug263Annotation(shared actual String string) 
        satisfies Annotation<Bug263Annotation> {}

annotation Bug263Annotation bug263annotation(String s) => Bug263Annotation(s);

bug263annotation("hello")
void bug263() {
    print(`function bug263`.annotations<Bug263Annotation>());
}