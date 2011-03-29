shared interface Equality<in T> 
        given T satisfies Equality<T> {
    
    doc "The equals operator |x == y|. Implementations should 
         respect the constraint that if |x===y| then |x==y|,
         the constraint that if |x==y| then |y==x|,
         and the constraint that if |x==y| and |y==z| then
         |x==z|."
    shared formal Boolean equals(T other);
    
    doc "The hash code of the instance. Implementations of |hash| 
         must respect the constraint that if |x==y| then 
         |x.hash==y.hash|."
    shared formal Integer hash;
    
}