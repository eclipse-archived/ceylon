"""A function model represents the model of a Ceylon function that you can invoke and inspect.
   
   A function is a toplevel binding, declared on a package.
   
   This is a [[FunctionModel]] that you can also invoke:
   
       shared String foo(String name) => "Hello "+name;
       
       void test(){
           Function<String,[String]> f = `foo`;
           // This will print: Hello Stef
           print(f("Stef"));
       }
 """
shared interface Function<out Type=Anything, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Callable<Type, Arguments>
        given Arguments satisfies Anything[] {
    
    "Type-unsafe function application, to be used when the argument types are unknown until runtime.
     
     This has the same behaviour as invoking this Function directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If any argument is not assignable to this function's corresponding parameter")
    throws(`class InvocationException`, "If there are not enough or too many provided arguments")
    shared formal Type apply(Anything* arguments);
}
