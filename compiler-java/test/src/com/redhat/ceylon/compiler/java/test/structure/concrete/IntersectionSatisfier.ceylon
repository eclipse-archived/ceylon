interface IntersectionSatisfier_X<out T, out N> given N satisfies Nothing {
    shared formal T|N x;
}
interface IntersectionSatisfier_I1<out T> satisfies IntersectionSatisfier_X<T, Nothing> {
    shared actual default T? x { return null; }
}
interface IntersectionSatisfier_I2 satisfies IntersectionSatisfier_I1<Void> {}
class IntersectionSatisfier_C<T>() satisfies IntersectionSatisfier_I2&IntersectionSatisfier_I1<T> {}