import ceylon.language.meta.declaration{ConstructorDeclaration,
    CallableConstructorDeclaration,
    ValueConstructorDeclaration}

@noanno
final annotation class ConstructorAnnotation() 
        satisfies OptionalAnnotation<ConstructorAnnotation, ConstructorDeclaration> {
    
}
@noanno
annotation ConstructorAnnotation constructorAnnotation() => ConstructorAnnotation();

@noanno
final annotation class CallableConstructorAnnotation() 
        satisfies OptionalAnnotation<CallableConstructorAnnotation, CallableConstructorDeclaration> {
    
}
@noanno
annotation CallableConstructorAnnotation callableConstructorAnnotation() => CallableConstructorAnnotation();


@noanno
final annotation class ValueConstructorAnnotation() 
        satisfies OptionalAnnotation<ValueConstructorAnnotation, ValueConstructorDeclaration> {
    
}
@noanno
annotation ValueConstructorAnnotation valueConstructorAnnotation() => ValueConstructorAnnotation();

class Constructor {
    "docannotation"
    constructorAnnotation
    callableConstructorAnnotation 
    shared new (s) {
         "ctor param doc"
         String s;
    }
    "docannotation"
    constructorAnnotation
    callableConstructorAnnotation 
    shared new other("ctor param doc" String s) {
        
    }
    
    "docannotation"
    constructorAnnotation
    valueConstructorAnnotation 
    shared new val {
        
    }
    
}

