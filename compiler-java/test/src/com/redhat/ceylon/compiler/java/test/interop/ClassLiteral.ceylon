import java.util{Map}
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
import ceylon.language.meta.model{Class}

@noanno
void classLiteral<T>() given T satisfies Object {
    // not a coercion point
    Class<Object> c = `String`;
    assert("ceylon.language::String"==c.string);
    // a coercion point, but not optimisable
    assert("class ceylon.language.String"==ClassLiteral.classString(c));
    
    // the rest 
    assert("class ceylon.language.String"==ClassLiteral.classString(`String`));
    assert("class ceylon.language.Integer"==ClassLiteral.classString(`Integer`));
    assert("class ceylon.language.Boolean"==ClassLiteral.classString(`Boolean`));
    assert("class ceylon.language.Float"==ClassLiteral.classString(`Float`));
    assert("class ceylon.language.Character"==ClassLiteral.classString(`Character`));
    assert("class ceylon.language.Byte"==ClassLiteral.classString(`Byte`));
    
    
    // erased
    assert("class java.lang.Object"==ClassLiteral.classString(`Object`));
    assert("class java.lang.Object"==ClassLiteral.classString(`Basic`));
    assert("class java.lang.Object"==ClassLiteral.classString(`Identifiable`));
    
    assert("class java.lang.Throwable"==ClassLiteral.classString(`Throwable`));
    assert("class java.lang.Exception"==ClassLiteral.classString(`Exception`));
    
    assert("interface java.lang.annotation.Annotation"==ClassLiteral.classString(`Annotation`));
    assert("interface java.lang.annotation.Annotation"==ClassLiteral.classString(`ConstrainedAnnotation`));
    
    // arrays
    assert("class [Lceylon.language.String;"==ClassLiteral.classString(`ObjectArray<String>`));
    assert("class [I"==ClassLiteral.classString(`IntArray`));
    assert("class [S"==ClassLiteral.classString(`ShortArray`));
    assert("class [J"==ClassLiteral.classString(`LongArray`));
    assert("class [B"==ClassLiteral.classString(`ByteArray`));
    assert("class [C"==ClassLiteral.classString(`CharArray`));
    assert("class [F"==ClassLiteral.classString(`FloatArray`));
    assert("class [D"==ClassLiteral.classString(`DoubleArray`));
    assert("class [Z"==ClassLiteral.classString(`BooleanArray`));
    
    // arrays of erased 
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Object>`));
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Basic>`));
    assert("class [Ljava.lang.Object;"==ClassLiteral.classString(`ObjectArray<Identifiable>`));
    
    assert("class [Ljava.lang.Throwable;"==ClassLiteral.classString(`ObjectArray<Throwable>`));
    assert("class [Ljava.lang.Exception;"==ClassLiteral.classString(`ObjectArray<Exception>`));
    
    assert("class [Ljava.lang.annotation.Annotation;"==ClassLiteral.classString(`ObjectArray<Annotation>`));
    assert("class [Ljava.lang.annotation.Annotation;"==ClassLiteral.classString(`ObjectArray<ConstrainedAnnotation>`));

    // arrays of arrays
    assert("class [[I"==ClassLiteral.classString(`ObjectArray<IntArray>`));
    assert("class [[Lceylon.language.String;"==ClassLiteral.classString(`ObjectArray<ObjectArray<String>>`));
    
    // generics
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<String>`));
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<T>`));
    assert("interface ceylon.language.Set"==ClassLiteral.classString(`Set<T&Float>`));
    
    // member classes and interfaces
    assert("class com.redhat.ceylon.compiler.java.test.interop.ClassLiteral$MemberClass"==ClassLiteral.classString(`ClassLiteral.MemberClass`));
    assert("class com.redhat.ceylon.compiler.java.test.interop.ClassLiteral$StaticMemberClass"==ClassLiteral.classString(`ClassLiteral.StaticMemberClass`));
    assert("interface java.util.Map$Entry"==ClassLiteral.classString(`Map<String,String>.Entry<String,String>`));
}

shared void classLiteral_run() {
    classLiteral<String>();
}