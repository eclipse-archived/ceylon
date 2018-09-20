import java.util{
    Map
}
import java.lang{
    ObjectArray,
    IntArray,
    ShortArray,
    LongArray,
    ByteArray,
    CharArray,
    FloatArray,
    DoubleArray,
    BooleanArray
}
import ceylon.language.meta.model{
    Class,
    ClassOrInterface
}

@noanno
void classLiteral<T>() given T satisfies Object {
    // not a coercion point
    Class<Object> c = `String`;
    assert("ceylon.language::String"==c.string);
    // a coercion point, but not optimisable
    assert("class ceylon.language.String"==ClassLiteral.classString(c));
    
    // the rest
    assert("class ceylon.language.String"==ClassLiteral.classString(`String`));
    variable ClassOrInterface<Object> x = `String`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`String`));
     
    assert("class ceylon.language.Integer"==ClassLiteral.classString(`Integer`));
     x = `Integer`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Integer`));
    
    assert("class ceylon.language.Boolean"==ClassLiteral.classString(`Boolean`));
    x = `Boolean`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Boolean`));
    
    assert("class ceylon.language.Float"==ClassLiteral.classString(`Float`));
    x = `Float`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Float`));
    
    assert("class ceylon.language.Character"==ClassLiteral.classString(`Character`));
    x = `Character`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Character`));
    
    assert("class ceylon.language.Byte"==ClassLiteral.classString(`Byte`));
    x = `Byte`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Byte`));
    
    
    // erased
    assert("class java.lang.Object"==ClassLiteral.classString(`Object`));
    x = `Object`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Object`));
    
    assert("class java.lang.Object"==ClassLiteral.classString(`Basic`));
    x = `Basic`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Basic`));
    
    assert("class java.lang.Object"==ClassLiteral.classString(`Identifiable`));
    x = `Identifiable`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Identifiable`));
    
    assert("class java.lang.Throwable"==ClassLiteral.classString(`Throwable`));
    x = `Throwable`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Throwable`));
    
    assert("class java.lang.Exception"==ClassLiteral.classString(`Exception`));
    x = `Exception`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Exception`));
    
    assert("interface java.lang.annotation.Annotation"==ClassLiteral.classString(`Annotation`));
    x = `Annotation`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Annotation`));
    
    assert("interface java.lang.annotation.Annotation"==ClassLiteral.classString(`ConstrainedAnnotation<>`));
    x = `ConstrainedAnnotation<>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ConstrainedAnnotation<>`));
    
    // arrays
    assert("class [Lceylon.language.String;"==ClassLiteral.classString(`ObjectArray<String>`));
    x = `ObjectArray<String>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<String>`));
    
    assert("class [I"==ClassLiteral.classString(`IntArray`));
    x = `IntArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`IntArray`));
    assert("class [S"==ClassLiteral.classString(`ShortArray`));
    x = `ShortArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ShortArray`));
    assert("class [J"==ClassLiteral.classString(`LongArray`));
    x = `LongArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`LongArray`));
    assert("class [B"==ClassLiteral.classString(`ByteArray`));
    x = `ByteArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ByteArray`));
    assert("class [C"==ClassLiteral.classString(`CharArray`));
    x = `CharArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`CharArray`));
    assert("class [F"==ClassLiteral.classString(`FloatArray`));
    x = `FloatArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`FloatArray`));
    assert("class [D"==ClassLiteral.classString(`DoubleArray`));
    x = `DoubleArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`DoubleArray`));
    assert("class [Z"==ClassLiteral.classString(`BooleanArray`));
    x = `BooleanArray`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`BooleanArray`));
    
    // arrays of erased 
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Object>`));
    x = `ObjectArray<Object>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Object>`));
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Basic>`));
    x = `ObjectArray<Basic>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Basic>`));
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Identifiable>`));
    x = `ObjectArray<Identifiable>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Identifiable>`));
    
    assert("class [Ljava.lang.Throwable;"==ClassLiteral.classString(`ObjectArray<Throwable>`));
    x = `ObjectArray<Throwable>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Throwable>`));
    assert("class [Ljava.lang.Exception;"==ClassLiteral.classString(`ObjectArray<Exception>`));
    x = `ObjectArray<Exception>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Exception>`));
    
    assert("class [Ljava.lang.annotation.Annotation;"==ClassLiteral.classString(`ObjectArray<Annotation>`));
    x = `ObjectArray<Annotation>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<Annotation>`));
    assert("class [Ljava.lang.annotation.Annotation;"==ClassLiteral.classString(`ObjectArray<ConstrainedAnnotation<>>`));
    x = `ObjectArray<ConstrainedAnnotation<>>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<ConstrainedAnnotation<>>`));

    // arrays of arrays
    assert("class [[I"==ClassLiteral.classString(`ObjectArray<IntArray>`));
    x = `ObjectArray<IntArray>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<IntArray>`));
    assert("class [[Lceylon.language.String;"==ClassLiteral.classString(`ObjectArray<ObjectArray<String>>`));
    x = `ObjectArray<ObjectArray<String>>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ObjectArray<ObjectArray<String>>`));
    
    // generics
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<String>`));
    x = `Set<String>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Set<String>`));
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<T>`));
    x = `Set<T>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Set<T>`));
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<T&Float>`));
    x = `Set<T&Float>`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Set<T&Float>`));
    
    // member classes and interfaces
    assert("class org.eclipse.ceylon.compiler.java.test.interop.ClassLiteral$MemberClass"==ClassLiteral.classString(`ClassLiteral.MemberClass`));
    x = `ClassLiteral.MemberClass`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ClassLiteral.MemberClass`));
    assert("class org.eclipse.ceylon.compiler.java.test.interop.ClassLiteral$StaticMemberClass"==ClassLiteral.classString(`ClassLiteral.StaticMemberClass`));
    x = `ClassLiteral.StaticMemberClass`;
    assert(ClassLiteral.classString(x)==ClassLiteral.classString(`ClassLiteral.StaticMemberClass`));
    assert("interface java.util.Map$Entry"==ClassLiteral.classString(`Map<String,String>.Entry<String,String>`));
    //x = `Map<String,String>.Entry<String,String>`;
    //assert(ClassLiteral.classString(x)==ClassLiteral.classString(`Map<String,String>.Entry<String,String>`));
}

shared void classLiteral_run() {
    classLiteral<String>();
}