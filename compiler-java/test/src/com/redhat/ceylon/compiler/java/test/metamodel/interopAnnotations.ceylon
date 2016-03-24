import javax.inject{
    Inject,
    inject
}
import com.redhat.ceylon.compiler.java.test.metamodel.annotations {
    Type, 
    Constructor, 
    TypeConstructor,
    type, 
    constructor, 
    typeConstructor
}

class JavaxInject() {
    inject 
    shared variable String attribute = "";
}

type
constructor
typeConstructor
class ClassWithInit() {
    
}

type
class ClassWithCtor{
    constructor
    typeConstructor
    shared new () {
        
    }
}

shared void interopAnnotations() {
    assert(!`value JavaxInject.attribute`.annotations<VariableAnnotation>().empty);
    assert(!`value JavaxInject.attribute`.annotations<Inject>().empty);
    
    assert(!`class ClassWithInit`.annotations<Type>().empty);
    assert(!`class ClassWithInit`.annotations<Constructor>().empty);
    assert(!`class ClassWithInit`.annotations<TypeConstructor>().empty);
    
    assert(!`class ClassWithCtor`.annotations<Type>().empty);
    assert(`class ClassWithCtor`.annotations<Constructor>().empty);
    assert(`class ClassWithCtor`.annotations<TypeConstructor>().empty);
    
    assert(`new ClassWithCtor`.annotations<Type>().empty);
    assert(!`new ClassWithCtor`.annotations<Constructor>().empty);
    assert(!`new ClassWithCtor`.annotations<TypeConstructor>().empty);
}