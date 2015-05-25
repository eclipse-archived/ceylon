import ceylon.language { AnnotationType = Annotation }
import ceylon.language.meta.model {
    Class,
    MemberClass,
    AppliedType = Type,
    IncompatibleTypeException,
    TypeApplicationException
}

"""Class declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel classes
   
   Because some classes have type parameters, getting a model requires applying type arguments to the
   class declaration with [[classApply]] in order to be able to instantiate that class. For example, here is how you would
   obtain a class model that you can instantiate from a toplevel class declaration:
   
       class Foo<T>(){
           string => "Hello, our T is: ``typeLiteral<T>()``";
       }
       
       void test(){
           // We need to apply the Integer closed type to the Foo declaration in order to get the Foo<Integer> closed type
           Class<Foo<Integer>,[]> classModel = `class Foo`.classApply<Foo<Integer>,[]>(`Integer`);
           // This will print: Hello, our T is: ceylon.language::Integer
           print(classModel());
       }
   
   <a name="member-sample"></a>
   ### Usage sample for member classes

   For member classes it is a bit longer, because member classes need to be applied not only their type arguments but also
   the containing type, so you should use [[memberClassApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared class Inner(){
               string => "Hello";
           }
       }

       void test(){
           // apply the containing closed type `Outer` to the member class declaration `Outer.Inner`
           MemberClass<Outer,Outer.Inner,[]> memberClassModel = `class Outer.Inner`.memberClassApply<Outer,Outer.Inner,[]>(`Outer`);
           // We now have a MemberClass, which needs to be applied to a containing instance in order to become an
           // invokable class model:
           Class<Outer.Inner,[]> boundMemberClassModel = memberClassModel(Outer());
           // This will print: Hello
           print(boundMemberClassModel());
       }
   """
shared sealed interface ClassDeclaration 
        of ClassWithInitializerDeclaration | ClassWithConstructorsDeclaration
        satisfies ClassOrInterfaceDeclaration & FunctionalDeclaration {
    
    "True if the class has an [[abstract|ceylon.language::abstract]] annotation."
    shared formal Boolean abstract;

    "True if the class is serializable class."
    shared formal Boolean serializable;

    "True if the class is an anonymous class, as is the case for the class of object value declarations."
    shared formal Boolean anonymous;

    "This anonymous class' object value declaration if this class is an anonymous class. `null` otherwise."
    shared formal ValueDeclaration? objectValue;

    "True if the class has a [[final|ceylon.language::final]] annotation."
    shared formal Boolean final;

    "Applies the given closed type arguments to this toplevel class declaration in order to obtain a class model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Type` or `Arguments` type arguments are not compatible with the actual result.")
    throws(`class TypeApplicationException`, "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Class<Type, Arguments> classApply<Type=Anything, Arguments=Nothing>(AppliedType<>* typeArguments)
        given Arguments satisfies Anything[];

    "Applies the given closed container type and type arguments to this member class declaration in order to obtain a member class model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Container`, `Type` or `Arguments` type arguments are not compatible with the actual result.")
    throws(`class TypeApplicationException`, "If the specified closed container type or type argument values are not compatible with the actual result's container type or type parameters.")
    shared formal MemberClass<Container, Type, Arguments> memberClassApply<Container=Nothing, Type=Anything, Arguments=Nothing>(AppliedType<Object> containerType, AppliedType<>* typeArguments)
        given Arguments satisfies Anything[];

    "Creates a new instance of this toplevel class, by applying the specified type arguments and value arguments."
    throws(`class IncompatibleTypeException`, "If the specified type or value arguments are not compatible with this toplevel class.")
    shared default Object instantiate(AppliedType<>[] typeArguments = [], Anything* arguments)
        => classApply<Object, Nothing>(*typeArguments).apply(*arguments);
    
    "Creates a new instance of this member class, by applying the specified type arguments and value arguments."
    throws(`class IncompatibleTypeException`, "If the specified container, type or value arguments are not compatible with this method.")
    shared default Object memberInstantiate(Object container, AppliedType<>[] typeArguments = [], Anything* arguments)
        => memberClassApply<Nothing, Object, Nothing>(`Nothing`, *typeArguments).bind(container).apply(*arguments);
    
    "Looks up a constructor declaration directly declared on this class, by name. 
     Returns `null` if no such constructor matches. 
     This includes unshared constructors but not inherited constructors 
     (since constructors are not members)."
    shared formal ConstructorDeclaration? getConstructorDeclaration(String name);
    
    "The default constructor declaration of this class. 
     Returns null if this class lacks a default constructor.
     The default constructor is the one with the name `\"\"`
     (the empty string)."
    shared formal ConstructorDeclaration? defaultConstructorDeclaration;
    
    "Returns the list of constructors declared on this class. This includes unshared constructors."
    shared formal ConstructorDeclaration[] constructorDeclarations();
    
    "Returns the list of constructors declared on this class that are annotated with the 
     given `Annotation` type argument. This includes unshared constructors."
    shared formal ConstructorDeclaration[] annotatedConstructorDeclarations<Annotation>()
            given Annotation satisfies AnnotationType;
}

shared sealed interface ClassWithInitializerDeclaration 
        satisfies ClassDeclaration {
    shared actual default [] constructorDeclarations() => [];
    shared actual default Null defaultConstructorDeclaration => null;
    shared actual default Null getConstructorDeclaration(String name) => null;
    shared actual default [] annotatedConstructorDeclarations<Annotation>()
            given Annotation satisfies AnnotationType => [];
}

shared sealed interface ClassWithConstructorsDeclaration 
        satisfies ClassDeclaration {
    shared actual default Nothing instantiate(AppliedType<>[] typeArguments, Anything* arguments) {
        throw IncompatibleTypeException("class has constructors");
    }
    shared actual default Nothing memberInstantiate(Object container, AppliedType<>[] typeArguments, Anything* arguments) {
        throw IncompatibleTypeException("class has constructors");
    }
}