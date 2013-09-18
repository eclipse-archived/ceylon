import ceylon.language.meta.declaration { VariableDeclaration }

"""A variable attribute model represents the model of a Ceylon variable attribute that you 
   can read, write and inspect.
   
   A variable attribute is a member attribute, it is declared on classes or interfaces.
   
   This is both an [[Attribute]] and a [[Member]]: you can invoke it with an instance value
   to bind it to that instance and obtain an [[Attribute]]:
   
       class Outer(){
           shared variable String foo = "Hello";
       }
       
       void test(){
           VariableAttribute<Outer,String> attribute = `Outer.foo`;
           Variable<String> boundAttribute = attribute(Outer());
           // This will print: Hello
           print(boundAttribute.get());
           boundAttribute.set("Bonjour");
           // This will print: Bonjour
           print(boundAttribute.get());
       }
 """
shared interface VariableAttribute<in Container, Type>
        satisfies Member<Container, Variable<Type>> & Attribute<Container, Type> {
    
    "The variable declaration for this variable attribute."
    shared formal actual VariableDeclaration declaration;
}
