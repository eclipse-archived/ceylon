import ceylon.language.meta.declaration { Declaration, ClassOrInterfaceDeclaration }

@nomodel
final annotation class VariadicTricks(String* args) satisfies SequencedAnnotation<VariadicTricks, ClassOrInterfaceDeclaration>{}
@nomodel
annotation VariadicTricks variadicTricksEmpty() => VariadicTricks();
@nomodel
annotation VariadicTricks variadicTricksRepeated(String s) => VariadicTricks(s, s);
@nomodel
annotation VariadicTricks variadicTricks(String* args) => VariadicTricks(*args);
@nomodel
annotation VariadicTricks variadicTricksIndirect(String* args) => variadicTricks(*args);
@nomodel
annotation VariadicTricks variadicTricksIndirectEmpty() => variadicTricksIndirect();
@nomodel
annotation VariadicTricks variadicTricksIndirectRepeated(String s) => variadicTricksIndirect(s, s);
@nomodel
variadicTricksEmpty
variadicTricksRepeated("repeated")
variadicTricksIndirect
variadicTricksIndirect("indirect")
variadicTricksIndirectEmpty
variadicTricksIndirectRepeated("indirect-repeated")
class VariadicTricksCallsite(){}