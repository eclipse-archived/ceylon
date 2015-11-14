
@noanno
shared void bug2321() {
    value f1 = Tuple<String|Integer,String,[Integer]>.first;
    value f2 = Tuple<Anything,String,[]>.first;
    value f = byIncreasing<[String, Anything*], String>(Tuple.first);
}
