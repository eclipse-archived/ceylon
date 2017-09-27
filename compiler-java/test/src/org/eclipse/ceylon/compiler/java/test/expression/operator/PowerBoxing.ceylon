@noanno
void powerBoxing<T>(Exponentiable<Integer, Integer> intSelf,
    Exponentiable<Float, Float> floatSelf,
    T exponentiable) given T satisfies Exponentiable<T, Integer> {
    value exp = 2;
    value result2 = 2 ^ exp;
    value result3 = 2.0 ^ exp;
    value result4 = 2.0 ^ 2.0;
    value result5 = intSelf ^ exp;
    value result6 = floatSelf ^ 2.0;
    value result7 = exponentiable ^ exp;
    
}