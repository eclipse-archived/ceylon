@nomodel
class Bug1095B() {
    void selfType<Summand>(Summand x) 
        given Summand satisfies Summable<Summand> {
        Summand y = x.plus{other=x;};
        Summable<Summand> y2 = x.plus{other=x;};
        Summable<Summand> x2 = x;
        Summable<Summand> y3 = x.plus{other=x2 of Summand;};
    }
}