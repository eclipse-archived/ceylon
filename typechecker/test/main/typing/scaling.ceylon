class Scaling() {

shared interface Scalable<Scale,Result> of Result 
        given Result satisfies Scalable<Scale,Result> {
    shared formal Result scale(Scale s);
}

shared interface Subtractable<Other> of Other
        satisfies Summable<Other>
        given Other satisfies Subtractable<Other> {
    shared formal Other subtract(Other other);
}

shared interface Numeric<Other> of Other
        satisfies Subtractable<Other> & 
                  Invertable<Other> & 
                  Scalable<Other,Other>
        given Other satisfies Numeric<Other> {

    shared Result times<Result>(Result other)
            given Result satisfies Scalable<Other,Result> 
            => other.scale(this of Other);

    shared formal Other divided(Other other);

}

interface Vec<Num> satisfies Scalable<Num,Vec<Num>>&[Num+] {}
interface Num satisfies Numeric<Num> {}

void test(Num n, Vec<Num> ns)  {
    @type:"Scaling.Num" value n1 = n.times(n);
    @type:"Scaling.Vec<Scaling.Num>" value ns1 = n.times(ns);
}

}