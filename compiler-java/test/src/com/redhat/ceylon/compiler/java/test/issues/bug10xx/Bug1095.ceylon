@noanno
class Bug1095() {
    shared Value variadicSum<Value>(Value* values) 
        given Value satisfies Summable<Value> {
        return nothing;
    }
    shared Value defaultedSum<Value>({Value*} dummy={}) 
        given Value satisfies Summable<Value> {
        return nothing;
    }

    void constrainedTypeParameter<Summand>({Summand+} values) 
        given Summand satisfies Summable<Summand> {
        Summand x = sum(values);
        Summand y = sum{values=values;};
        Summand z = variadicSum<Summand>{};
        Summand a = defaultedSum<Summand>{};
    }
    
}