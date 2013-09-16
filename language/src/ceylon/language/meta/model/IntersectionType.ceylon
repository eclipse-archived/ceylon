shared interface IntersectionType<out Intersection=Anything> 
        satisfies Type<Intersection> {
    shared formal List<Type<Anything>> satisfiedTypes;
}
