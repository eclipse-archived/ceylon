import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@nomodel
annotation class AnnotationClassInteger(Integer i, Integer j=1) satisfies SequencedAnnotation<AnnotationClassInteger, Type<Anything>>{}
@nomodel
annotation AnnotationClassInteger annotationClassIntegerDefault(Integer i) 
    => AnnotationClassInteger(i);
@nomodel
annotation AnnotationClassInteger annotationClassIntegerSwapped(Integer j, Integer i) 
    => AnnotationClassInteger(i, j);
@nomodel
annotation AnnotationClassInteger annotationClassIntegerNames(Integer jj, Integer ii) 
    => AnnotationClassInteger{
            i=ii;
            j=jj;
       };
@nomodel
annotation AnnotationClassInteger annotationClassIntegerStatic() 
    => AnnotationClassInteger{
            i=40;
            j=41;
       };
@nomodel
annotationClassIntegerDefault{i=10;}
annotationClassIntegerSwapped{j=21; i=20;}
annotationClassIntegerNames{ii=30; jj=31;}
annotationClassIntegerStatic{}
class AnnotationClassInteger_callsite(){}