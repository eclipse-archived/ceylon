import ceylon.language.meta.declaration{CallableConstructorDeclaration, TypeParameter}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A callable constructor model represents the model of a Ceylon class 
   constructor that you can invoke and inspect
   
   ## Callablity
   
   As with [[Function]] you can also invoke a `CallableConstructor`, doing so 
   instantiates an instance:
   
        shared class Foo {
            shared String name;
            shared new foo(String name) {
                this.name = name;
            }
        }
        
        void test() {
        Constructor<Foo,[String]> ctor = `Foo.foo`;
        // This will print: Stef
        print(ctor("Stef").name);
        
   ## Genericity
        
   This class inherits [[Generic]] but a constructor in Ceylon cannot 
   have a type parameters. 
   For symmetry with [[CallableConstructorDeclaration.apply]] the 
   [[typeArguments]] and [[typeArgumentList]] refer to the type arguments 
   of the constructor's class.
   """
shared sealed interface CallableConstructor<out Type=Object, in Arguments=Nothing>
        satisfies Function<Type, Arguments> 
        given Arguments satisfies Anything[] {
    
    "This constructor's declaration."
    shared formal actual CallableConstructorDeclaration declaration;
    
    shared formal actual Class<Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal Class<Type> container;
    
}

shared sealed interface MemberClassCallableConstructor<in Container=Nothing, out Type=Object, in Arguments=Nothing>
        satisfies Method<Container, Type, Arguments>
        given Arguments satisfies Anything[] {


    "This constructor's declaration."
    shared formal actual CallableConstructorDeclaration declaration;
    
    shared formal actual MemberClass<Container, Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal MemberClass<Container,Type> container;

    shared actual formal CallableConstructor<Type, Arguments> bind(Object container);
}
