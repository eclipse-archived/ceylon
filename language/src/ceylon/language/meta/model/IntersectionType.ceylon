"A closed intersection type."
shared sealed interface IntersectionType<out Intersection> 
        satisfies Type<Intersection> {
    
    "The list of closed satisfied types of this intersection."
    shared formal List<Type<>> satisfiedTypes;
}
