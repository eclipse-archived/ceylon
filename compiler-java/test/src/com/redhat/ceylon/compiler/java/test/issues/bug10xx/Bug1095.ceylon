@nomodel
class Bug1095() {
    void constrainedTypeParameter<Summand>({Summand+} values) 
        given Summand satisfies Summable<Summand> {
        Summand x = sum(values);
        Summand y = sum{values=values;};
    }
}