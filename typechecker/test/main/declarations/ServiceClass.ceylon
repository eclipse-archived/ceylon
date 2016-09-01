shared interface ServiceInterface {}
shared abstract class ServiceClass() {}

@error:"declaration is an abstract class, and may not be annotated service"
service(`interface ServiceInterface`)
shared abstract class AbstractImplementation() satisfies ServiceInterface {
}

@error:"declaration is not a shared top level class, and may not be annotated service"
service(`interface ServiceInterface`)
class NonSharedImplementation() satisfies ServiceInterface {
}

service(`interface ServiceInterface`)
shared class ValidImplementation() satisfies ServiceInterface {
    @error:"declaration is not a shared top level class, and may not be annotated service"
    service(`interface ServiceInterface`)
    class Inner() satisfies ServiceInterface {
        
    }
}

service(`class ServiceClass`)
shared class ValidImplementationClass() extends ServiceClass() {
}

@error:"service class does not implement service 'interface ServiceInterface'"
service(`interface ServiceInterface`)
shared class ImplementationWithoutSatisfy() {
    
}

@error:"service class may not be generic"
service(`interface ServiceInterface`)
shared class ImplementationGeneric<T>() satisfies ServiceInterface {
    
}

@error:"service class have a parameter list or default constructor and be instantiable with an empty argument list"
service(`interface ServiceInterface`)
shared class ImplementationWithoutNullary(String p) satisfies ServiceInterface {
    
}

@error:"service class have a parameter list or default constructor and be instantiable with an empty argument list"
service(`interface ServiceInterface`)
shared class ImplementationWithoutNullaryConstructor satisfies ServiceInterface {
    shared new (String p) {}
}

service(`interface ServiceInterface`)
shared class ImplementationWithNullary(String? p=null) satisfies ServiceInterface {
}

service(`interface ServiceInterface`)
shared class ImplementationWithNullaryConstructor satisfies ServiceInterface {
    shared new (String? p=null) {}
}

//@error:"service must be an interface or abstract class"
service(`class ValidImplementation`)
shared class ImplementationWithBadAnnotation() extends ValidImplementation() {
}
