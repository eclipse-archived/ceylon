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
}
