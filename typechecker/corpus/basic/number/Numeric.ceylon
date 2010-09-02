public interface Numeric<N, I>
        satisfies Number, Comparable<N> 
        given I satisfies Number 
        given N satisfies Number {

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