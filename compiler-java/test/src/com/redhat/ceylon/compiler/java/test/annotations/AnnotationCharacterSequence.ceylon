import ceylon.language.meta.declaration {ClassDeclaration}
import ceylon.language.meta{optionalAnnotation} 

final annotation class AnnotationCharacterSequence(shared Sequential<Character> s = ['g', 'r', 'r','r'])
    satisfies OptionalAnnotation<AnnotationCharacterSequence, ClassDeclaration>{
    
}
annotation AnnotationCharacterSequence annotationCharacterSequence(Sequential<Character> s)
    => AnnotationCharacterSequence(s);

annotationCharacterSequence(['g', 'r', 'r','r'])
class AnnotationCharacterSequenceUse() {
    assert(exists got=optionalAnnotation(`AnnotationCharacterSequence`, `class AnnotationCharacterSequenceUse`),
        ['g', 'r', 'r','r'] == got.s);
}