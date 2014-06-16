import ceylon.language.meta.declaration {
    Module, Package, NestableDeclaration, ValueDeclaration,
    FunctionDeclaration, AliasDeclaration, ClassOrInterfaceDeclaration
}
import ceylon.language { AnnotationType = Annotation }

native class Paquete(name, container, shared Anything _pkg) satisfies Package {
    shared actual Module container;
    shared actual String name;
    shared actual String qualifiedName => name;
    shared actual native Boolean shared;
    shared actual native Kind[] members<Kind>()
            given Kind satisfies NestableDeclaration;
    shared actual native Kind[] annotatedMembers<Kind, Annotation>()
            given Kind satisfies NestableDeclaration
            given Annotation satisfies AnnotationType;
    shared actual native Kind? getMember<Kind>(String name)
            given Kind satisfies NestableDeclaration;
    shared actual native ValueDeclaration? getValue(String name);
    shared actual native ClassOrInterfaceDeclaration? getClassOrInterface(String name);
    shared actual native FunctionDeclaration? getFunction(String name);
    shared actual native AliasDeclaration? getAlias(String name);
    shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
    shared native String suffix;
    string = "package " + name;
}
