@nomodel
abstract class SelfType<T>() of T given T satisfies SelfType<T> {
    shared formal Integer compareTo(T other);
    shared Integer reverseCompareTo(T other) {
        return other.compareTo(this);
    }
    shared T self() {
        T x = this;
        return this;
    }
}
@nomodel
void selfType<T>(SelfType<T> x, SelfType<T> y) 
        given T satisfies SelfType<T> {
    x.compareTo(y);
}