public interface Integral<N, I>
        satisfies Numeric<N, I>, Ordinal 
        given I satisfies Number 
        given N satisfies Number {

    doc "The binary % operator"
    public N remainder(N number);

}