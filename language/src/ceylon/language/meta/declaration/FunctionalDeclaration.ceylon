"Declaration which can be invoked, and may have parameters. Classes
 and functions are functional declarations.
 
 Note that multiple-parameter lists are not reified at runtime, so if you
 have a function declaration like this:
 
     Integer add(Integer a)(Integer b) => a + b;
 
 It will only have a single parameter list at runtime (the first), and its return
 type will be `Callable<Integer,[Integer]>`.
 "
shared interface FunctionalDeclaration {
    
    "True if the current declaration is an annotation class or function."
    shared formal Boolean annotation;
    
    "The list of parameter declarations for this functional declaration."
    shared formal FunctionOrValueDeclaration[] parameterDeclarations;
    
    "Gets a parameter declaration by name. Returns `null` if no such parameter exists."
    shared formal FunctionOrValueDeclaration? getParameterDeclaration(String name);
}