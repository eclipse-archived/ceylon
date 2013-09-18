import ceylon.language.meta.model { 
    Function,
    Type,
    Method,
    IncompatibleTypeException,
    TypeApplicationException
}

"""A function declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel function
   
   Because some functions have type parameters, getting a model requires applying type arguments to the
   function declaration with [[apply]] in order to be able to invoke that function. For example, here is how you would
   obtain a function model that you can invoke from a toplevel function declaration:
   
       String foo<T>(){
           return "Hello, our T is: ``typeLiteral<T>()``";
       }
       
       void test(){
           // We need to apply the Integer closed type to the foo declaration in order to get the foo<Integer> function model
           Function<String,[]> functionModel = `function foo`.apply<String,[]>(`Integer`);
           // This will print: Hello, our T is: ceylon.language::Integer
           print(functionModel());
       }
   
   <a name="member-sample"></a>
   ### Usage sample for methods
	
   For methods it is a bit longer, because methods need to be applied not only their type arguments but also
   the containing type, so you should use [[memberApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared String hello() => "Hello";
       }
	
       void test(){
           // apply the containing closed type `Outer` to the method declaration `Outer.hello`
           Method<Outer,String,[]> methodModel = `function Outer.hello`.memberApply<Outer,String,[]>(`Outer`);
           // We now have a Method, which needs to be applied to a containing instance in order to become an
           // invokable function:
           Function<String,[]> boundMethodModel = methodModel(Outer());
           // This will print: Hello
           print(boundMethodModel());
       }
   """
shared interface FunctionDeclaration
        satisfies FunctionOrValueDeclaration & GenericDeclaration & FunctionalDeclaration {

    "Applies the given closed type arguments to this function declaration in order to obtain a function model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Return` or `Arguments` type arguments are not compatible with the actual result.")
    throws(`class TypeApplicationException`, "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Function<Return, Arguments> apply<Return=Anything, Arguments=Nothing>(Type<Anything>* typeArguments)
        given Arguments satisfies Anything[];

    "Applies the given closed container type and type arguments to this method declaration in order to obtain a method model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Container`, `Return` or `Arguments` type arguments are not compatible with the actual result.")
    throws(`class TypeApplicationException`, "If the specified closed container type or type argument values are not compatible with the actual result's container type or type parameters.")
    shared formal Method<Container, Return, Arguments> memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(Type<Container> containerType, Type<Anything>* typeArguments)
        given Arguments satisfies Anything[];
}