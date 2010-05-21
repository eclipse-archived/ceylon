public interface Integral<N, I>
        satisfies Numeric<N, I>, Ordinal 
        where I >= Number & N = subtype {

    doc "The binary % operator"
    public N remainder(N number);

}