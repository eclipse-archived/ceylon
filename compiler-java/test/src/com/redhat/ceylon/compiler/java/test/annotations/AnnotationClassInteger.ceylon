import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@noanno
annotation class AnnotationClassInteger(Integer i, Integer j=1) satisfies SequencedAnnotation<AnnotationClassInteger, Type<Anything>>{}
@noanno
annotation AnnotationClassInteger annotationClassIntegerDefault(Integer i) 
    => AnnotationClassInteger(i);
@noanno
annotation AnnotationClassInteger annotationClassIntegerSwapped(Integer j, Integer i) 
    => AnnotationClassInteger(i, j);
@noanno
annotation AnnotationClassInteger annotationClassIntegerNames(Integer jj, Integer ii) 
    => AnnotationClassInteger{
            i=ii;
            j=jj;
       };
@noanno
annotation AnnotationClassInteger annotationClassIntegerStatic() 
    => AnnotationClassInteger{
            i=40;
            j=41;
       };
@noanno
annotationClassIntegerDefault{i=-10;}
annotationClassIntegerSwapped{j=21; i=20;}
annotationClassIntegerNames{ii=30; jj=31;}
annotationClassIntegerStatic{}
class AnnotationClassInteger_callsite(){}