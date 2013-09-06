import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
final annotation class ParameterDefaults(String s1, String s2=s1/*, String[] seq = [s1, s1, s2, s2]*/)
    satisfies SequencedAnnotation<ParameterDefaults, ClassOrInterfaceDeclaration>{
}
@nomodel
annotation ParameterDefaults parameterDefaults(String p1, String p2=p1/*, String[] pseq = [p1, p1, p2, p2]*/) => ParameterDefaults(p1, p2/*, pseq*/);

// TODO 
// annotation ParameterDefaults parameterDefaults2(String q1, ParameterDefaults pd = parameterDefaults(String q1, String q1)) => nothing
