/*
 * Do we really need to enforce that you can't 
 * ask for the annotations of a certain type for a
 * certain program element unless the annotation 
 * type can appear at that program element? Why not
 * just return no annotations?
 * 
 */

shared S annotations<T,S,C>(Type<ConstrainedAnnotation<T,S,C>> annotationType,
                            C programElement)
           given T satisfies ConstrainedAnnotation<T,S,C>
           //given S of (T?) | (T[])
           given C satisfies Annotated { throw; }

shared T? optionalAnnotation<T,C>(Type<OptionalAnnotation<T,C>> annotationType,
                                  C programElement)
        given T satisfies OptionalAnnotation<T,C>
        given C satisfies Annotated { 
    return annotations<T,T?,C>(annotationType, programElement); 
}

shared T[] sequencedAnnotations<T,C>(Type<SequencedAnnotation<T,C>> annotationType,
                                     C programElement)
        given T satisfies SequencedAnnotation<T,C>
        given C satisfies Annotated { 
    return annotations<T,T[],C>(annotationType, programElement); 
}