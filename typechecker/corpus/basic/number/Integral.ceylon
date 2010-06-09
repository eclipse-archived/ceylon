public interface Integral<N, I>
        satisfies Numeric<N, I>, Ordinal 
        where I satisfies Number 
        where N satisfies Number {

    doc "The binary % operator"
    public N remainder(N number);

}