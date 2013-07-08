"Abstract supertype of types that support scaling by
 a numeric factor. Implementations should generally
 respect the following constraints, where relevant:
 
 - `x == 1**x`
 - `-x == -1**x`
 - `x-x == 0**x`
 - `x+x == 2**x`"
shared interface Scalable<in Scale, out Value> of Value 
        given Value satisfies Scalable<Scale,Value> {
    shared formal Value scale(Scale other);
}