import ceylon.language.meta.model { Value, Attribute, AppliedType = Type, IncompatibleTypeException }

"""A value declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel value
   
   Getting a model requires applying type arguments to the
   value declaration with [[apply]] in order to be able to read that value. For example, here is how you would
   obtain a value model that you can read from a toplevel attribute declaration:
   
       String foo = "Hello";
       
       void test(){
           // We need to apply the the foo declaration in order to get the foo value model
           Value<String> valueModel = `value foo`.apply<String>();
           // This will print: Hello
           print(valueModel.get());
       }
   
   <a name="member-sample"></a>
   ### Usage sample for attributes
	
   For attributes it is a bit longer, because attributes need to be applied the containing type, so you should 
   use [[memberApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared String foo => "Hello";
       }
	
       void test(){
           // Apply the containing closed type `Outer` to the attribute declaration `Outer.foo`
           Attribute<Outer,String> valueModel = `value Outer.foo`.memberApply<Outer,String>(`Outer`);
           // We now have an Attribute, which needs to be applied to a containing instance in order to become a
           // readable value:
           Value<String> boundValueModel = valueModel(Outer());
           // This will print: Hello
           print(boundValueModel.get());
       }
   """
shared interface ValueDeclaration
        satisfies FunctionOrValueDeclaration {
    
    "True if this declaration is annotated with [[variable|ceylon.language::variable]]."
    shared formal Boolean variable;

    "Applies this value declaration in order to obtain a value model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Type` type argument is not compatible with the actual result.")
    shared formal Value<Type> apply<Type=Anything>();

    "Applies the given closed container type to this attribute declaration in order to obtain an attribute model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Container` or `Type` type arguments are not compatible with the actual result.")
    shared formal Attribute<Container, Type> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType);
}