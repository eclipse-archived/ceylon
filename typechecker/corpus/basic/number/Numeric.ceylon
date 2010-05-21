public interface Numeric<N, I>
        satisfies Number, Comparable<N> 
        where I >= Number & N = subtype {

    doc "The unary - operator"
    public I inverse;

    doc "The binary + operator"
    public N plus(N number);

    doc "The binary - operator"
    public I minus(N number);

    doc "The binary * operator"
    public N times(N number);

    doc "The binary / operator"
    public N divided(N number);

    doc "The binary ** operator"
    public N power(N number);
    
}