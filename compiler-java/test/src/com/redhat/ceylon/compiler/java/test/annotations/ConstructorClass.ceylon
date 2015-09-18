import ceylon.language.meta.declaration{...}

@nomodel
final annotation class ClassWithInitializerAnnotation() 
        satisfies OptionalAnnotation<ClassWithInitializerAnnotation, ClassWithInitializerDeclaration> {}

@nomodel
annotation ClassWithInitializerAnnotation classWithInitializerAnnotation() => ClassWithInitializerAnnotation();

@nomodel
final annotation class ClassWithConstructorsAnnotation() 
        satisfies OptionalAnnotation<ClassWithConstructorsAnnotation, ClassWithConstructorsDeclaration> {}

@nomodel
annotation ClassWithConstructorsAnnotation classWithConstructorsAnnotation() => ClassWithConstructorsAnnotation();

@nomodel
classWithInitializerAnnotation
class ClassWithInitializer() {}

@nomodel
classWithConstructorsAnnotation
class ClassWithConstructors {
    shared new() {}
}