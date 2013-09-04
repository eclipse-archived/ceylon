import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }


final annotation class SequenceDefaults(
    String[] seq = [], String[] seq2 = {}, String[] seq3 = empty,
    {String*} iter = [], {String*} iter2 = {}, {String*} iter3 = empty,
    [String*] tup = [], [String*] tup2 = {}, [String*] tup3 = empty)
        satisfies SequencedAnnotation<SequenceDefaults, ClassOrInterfaceDeclaration>{
}
final annotation class SequenceDefaultsNonempty(
    String[] seq = ["a"],
    {String*} iter = ["a"], {String*} iter2 = {"a"},
    [String*] tup = ["a"])
        satisfies SequencedAnnotation<SequenceDefaultsNonempty, ClassOrInterfaceDeclaration>{
}


annotation SequenceDefaults sequenceDefaults({String*} seq = []) 
    => SequenceDefaults(seq, [], {});

