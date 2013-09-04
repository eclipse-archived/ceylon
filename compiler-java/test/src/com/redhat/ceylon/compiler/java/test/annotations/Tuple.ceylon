import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

final annotation class Tuple(String[] tup)
    satisfies SequencedAnnotation<Tuple, ClassOrInterfaceDeclaration>{
}
annotation Tuple tupleLiteral() => Tuple(["tuple", "Literal"]);
annotation Tuple tupleDefaulted(String[] t = ["tuple", "Defaulted"]) => Tuple(t);
//annotation Tuple tuple([String, String] tup) => Tuple(tup);

tupleLiteral
tupleDefaulted
tupleDefaulted(["tuple"])
class Tuple_callsite() {
}