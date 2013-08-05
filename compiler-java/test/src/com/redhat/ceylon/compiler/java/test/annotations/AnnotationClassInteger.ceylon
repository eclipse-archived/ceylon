import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassInteger(Integer i, Integer j=1) satisfies SequencedAnnotation<AnnotationClassInteger, ClassOrInterfaceDeclaration>{}
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
annotationClassIntegerDefault{i=-10;}
annotationClassIntegerSwapped{j=21; i=20;}
annotationClassIntegerNames{ii=30; jj=31;}
annotationClassIntegerStatic{}
class AnnotationClassInteger_callsite(){}