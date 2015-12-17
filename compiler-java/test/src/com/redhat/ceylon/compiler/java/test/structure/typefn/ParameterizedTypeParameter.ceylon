class ParameterizedTypeParameter<T, U, V>()
            given T<X> satisfies X()
            given U<Y> satisfies Anything(Y)
            given V<Z> satisfies List<Z>
{
    T<String> attribute = nothing;
    String s = attribute();
    U<String> attribute2 = nothing;
    Anything s2 = attribute2("");
    V<String> attribute3 = nothing;
    String? s3 = attribute3[0];
}