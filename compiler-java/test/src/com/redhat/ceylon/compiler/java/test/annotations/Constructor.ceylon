import ceylon.language.meta.declaration{ConstructorDeclaration}

@noanno
final annotation class ConstructorAnnotation() 
        satisfies OptionalAnnotation<ConstructorAnnotation, ConstructorDeclaration> {
    
}
@noanno
annotation ConstructorAnnotation constructorAnnotation() => ConstructorAnnotation();

class Constructor {
    "docannotation"
    constructorAnnotation shared new (s) {
         "ctor param doc"
         String s;
    }
    "docannotation"
    constructorAnnotation shared new other("ctor param doc" String s) {
        
    }
}