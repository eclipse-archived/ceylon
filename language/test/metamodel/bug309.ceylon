import ceylon.language.meta { type }
import ceylon.language.meta.declaration { FunctionDeclaration }

shared interface Bug309Interface1 {
    shared formal List<String> m1();
}

doc("test annotation")
shared class Bug309Class1() satisfies Bug309Interface1 {
    shared actual List<String> m1() {
        print ("Invoked");
        return [];
    }
}

@test
shared void bug309() {
    Bug309Class1 clsInstance = Bug309Class1(); // not known at runtime;

    value cas = type(clsInstance).declaration.annotatedMemberDeclarations<FunctionDeclaration, DocAnnotation>();

    for (ca in cas) {
        if (exists ra = ca.annotations<DocAnnotation>().first) {
            value method = type(clsInstance).getMethod<Bug309Interface1, List<String>, []>("m1");
            if (exists method) {
                value m = method(clsInstance);
                List<String> result = m();
                print (result);
            }
        }
    }
}
