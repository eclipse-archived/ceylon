import ceylon.language.metamodel{Annotation, Annotated, Type, ConstrainedAnnotation}

@noanno
interface Bug1188 {

    shared formal Bug1188[] parents;

}
@noanno
abstract class AbstractBug1188(parents) satisfies Bug1188 {
    shared actual Bug1188[] parents;
} 

@noanno
class RefiningBug1188(parents) 
                extends AbstractBug1188(parents) {

    Bug1188[] parents;

    if (parents.size > 1) {
        throw Exception();
    }
    Bug1188? parent = parents[0];

    
}


