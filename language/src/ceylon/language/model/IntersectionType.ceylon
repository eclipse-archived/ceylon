shared interface IntersectionType<out Intersection> 
        satisfies Type<Intersection> {
    shared formal List<Type<Anything>> satisfiedTypes;
}
