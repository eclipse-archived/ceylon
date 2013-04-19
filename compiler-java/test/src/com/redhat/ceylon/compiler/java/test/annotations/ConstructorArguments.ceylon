import ceylon.language.metamodel{Type}

@noanno
class ConstructorArguments(
    String s = "s",
    Boolean b = true,
    Integer i = 0,
    Character c = 'c',
    Float f = 0.0) {}

@noanno
annotation class Foo(String foo) {
    
}

@noanno
annotation ConstructorArguments constructorArguments(
    // Simple types
    String s,
    Boolean b,
    Integer i,
    Character c,
    Float f,
    // Optional types
    // TODO String? os, // we use a hidden annotation method: boolean os$null() default false;
    // Union Types
    // TODO String|Integer ut
    // 1-dimensional sequences of simple types
    String[] ss,
    Boolean[] bs,
    Integer[] is_,
    Character[] cs,
    Float[] fs,
    // TODO: <String|Integer>[] uts; 
    // Metamodel types: Serialized as a String (rather than a java.util.Class)
    // The annotation method is annotated with @AnnotationAttributeType()
    // So we know how to parse it
    // TODO Type<ConstructorArguments> classType
    
    // Annotations (non recursive)
    Foo foo
    
    // Annotations (recursive)
    // TODO ConstructorArguments()
    

) => ConstructorArguments();
