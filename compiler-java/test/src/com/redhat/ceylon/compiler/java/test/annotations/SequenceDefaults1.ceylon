import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration, Declaration, ValueDeclaration }

//  Use an iterable type
final annotation class SequenceDefaults_String({String*} seq) 
    satisfies SequencedAnnotation<SequenceDefaults_String, ClassOrInterfaceDeclaration>{
}
// Use a sequence enumeration
annotation SequenceDefaults_String sequenceDefaults_String({String*} seq = {"a", "b", "c"}) 
    => SequenceDefaults_String(seq);
annotation SequenceDefaults_String sequenceDefaults_String_Empty({String*} seq = {}) 
    => SequenceDefaults_String(seq);
annotation SequenceDefaults_String sequenceArguments_String() 
    => SequenceDefaults_String({"A", "B", "C"});
annotation SequenceDefaults_String sequenceArguments_String_Empty() 
    => SequenceDefaults_String({});

// Use a iterable type
final annotation class SequenceDefaults_Boolean({Boolean*} seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Boolean, ClassOrInterfaceDeclaration>{
}
// Use a tuple literal
annotation SequenceDefaults_Boolean sequenceDefaults_Boolean({Boolean*} seq = [true, false]) 
    => SequenceDefaults_Boolean(seq);
annotation SequenceDefaults_Boolean sequenceDefaults_Boolean_Empty({Boolean *} seq = []) 
    => SequenceDefaults_Boolean(seq);
annotation SequenceDefaults_Boolean sequenceArguments_Boolean() 
    => SequenceDefaults_Boolean([false, true]);
annotation SequenceDefaults_Boolean sequenceArguments_Boolean_Empty() 
    => SequenceDefaults_Boolean([]);

// Use a tuple type
final annotation class SequenceDefaults_Character([Character*] seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Character, ClassOrInterfaceDeclaration>{
}
// Use a tuple literal
annotation SequenceDefaults_Character sequenceDefaults_Character([Character*] seq = ['a', 'b', 'c']) 
    => SequenceDefaults_Character(seq);
annotation SequenceDefaults_Character sequenceDefaults_Character_Empty([Character*] seq = []) 
    => SequenceDefaults_Character(seq);
annotation SequenceDefaults_Character sequenceArguments_Character() 
    => SequenceDefaults_Character(['A', 'B', 'C']);
annotation SequenceDefaults_Character sequenceArguments_Character_Empty() 
    => SequenceDefaults_Character([]);

// Use a sequential type
final annotation class SequenceDefaults_Float(Float[] seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Float, ClassOrInterfaceDeclaration>{
}
// Spell out empty 
annotation SequenceDefaults_Float sequenceDefaults_Float([Float*] seq = [1.0, 2.0, 3.0]) 
    => SequenceDefaults_Float(seq);
annotation SequenceDefaults_Float sequenceDefaults_Float_Empty([Float*] seq = empty) 
    => SequenceDefaults_Float(seq);
annotation SequenceDefaults_Float sequenceArguments_Float() 
    => SequenceDefaults_Float([4.0, 5.0, 6.0]);
annotation SequenceDefaults_Float sequenceArguments_Float_Empty() 
    => SequenceDefaults_Float(empty);

final annotation class SequenceDefaults_Integer({Integer*} seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Integer, ClassOrInterfaceDeclaration>{
}
annotation SequenceDefaults_Integer sequenceDefaults_Integer([Integer*] seq = [1, 2, 3]) 
    => SequenceDefaults_Integer(seq);
annotation SequenceDefaults_Integer sequenceDefaults_Integer_Empty([Integer*] seq = empty) 
    => SequenceDefaults_Integer(seq);
annotation SequenceDefaults_Integer sequenceArguments_Integer() 
    => SequenceDefaults_Integer([4, 5, 6]);
annotation SequenceDefaults_Integer sequenceArguments_Integer_Empty() 
    => SequenceDefaults_Integer(empty);

// Note here the difference between [empty] and empty!
final annotation class SequenceDefaults_Object({Empty*} seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Object, ClassOrInterfaceDeclaration>{
}
annotation SequenceDefaults_Object sequenceDefaults_Object([Empty*] seq = [empty, empty]) 
    => SequenceDefaults_Object(seq);
annotation SequenceDefaults_Object sequenceDefaults_Object_Empty([Empty*] seq = empty) 
    => SequenceDefaults_Object(seq);
annotation SequenceDefaults_Object sequenceArguments_Object() 
    => SequenceDefaults_Object([empty, empty, empty]);
annotation SequenceDefaults_Object sequenceArguments_Object_Empty() 
    => SequenceDefaults_Object(empty);

final annotation class SequenceDefaults_Declaration({Declaration*} seq) 
    satisfies SequencedAnnotation<SequenceDefaults_Declaration, ClassOrInterfaceDeclaration>{
}
annotation SequenceDefaults_Declaration sequenceDefaults_Declaration([ValueDeclaration*] seq = [`value empty`]) 
    => SequenceDefaults_Declaration(seq);
annotation SequenceDefaults_Declaration sequenceDefaults_Declaration_Empty([ValueDeclaration*] seq = empty) 
    => SequenceDefaults_Declaration(seq);
annotation SequenceDefaults_Declaration sequenceArguments_Declaration() 
    => SequenceDefaults_Declaration([`value empty`]);
annotation SequenceDefaults_Declaration sequenceArguments_Declaration_Empty() 
    => SequenceDefaults_Declaration(empty);


//final annotation class SequenceDefaults_Annotation({SequenceDefaults_String*} seq) 
//    satisfies SequencedAnnotation<SequenceDefaults_Annotation, ClassOrInterfaceDeclaration>{
//}
/*TODO 
annotation SequenceDefaults_Annotation sequenceDefaults_Annotation([SequenceDefaults_String*] seq = [SequenceDefaults_String(empty), SequenceDefaults_String([""])]) 
    => SequenceDefaults_Annotation(seq);
annotation SequenceDefaults_Annotation sequenceDefaults_Annotation_Empty({SequenceDefaults_String*} seq = {}) 
    => SequenceDefaults_Annotation(seq);
annotation SequenceDefaults_Annotation sequenceArguments_Annotation() 
    => SequenceDefaults_Annotation([sequenceDefaults_String]);
annotation SequenceDefaults_Annotation sequenceArguments_Annotation_Empty() 
    => SequenceDefaults_Annotation({});
*/