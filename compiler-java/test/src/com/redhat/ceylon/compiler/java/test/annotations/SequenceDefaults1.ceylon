import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }


final annotation class SequenceDefaults({String*} seq = [""], {String*} seq2 = {}, {String*} seq3 = empty)
        satisfies SequencedAnnotation<SequenceDefaults, ClassOrInterfaceDeclaration>{
}


annotation SequenceDefaults sequenceDefaults({String*} seq = []) 
    => SequenceDefaults(seq, [], {});

