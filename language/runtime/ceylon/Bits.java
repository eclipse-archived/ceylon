package ceylon;

public interface Bits<T> {

    /** Bitwise or operator |x | y| */
    public T or(T bits);

    /** Bitwise or operator |x & y| */
    public T and(T bits);

    /** Bitwise xor operator |x ^ y| */
    public T xor(T bits);

    /** Bitwise complement operator |~x| */
    public T complement();
}
