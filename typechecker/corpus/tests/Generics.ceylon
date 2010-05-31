class Generics {

    interface Processor<out X, in Y> {
        X process(Y y);
    }
    
    class ProcessorImpl<out X, in Y>(X p(Y y)) {
        X process(Y y) {
            return p(y)
        }
    }
    
    String stringify<Y>(Y y) where Y>=Number { return $y }
    String zero = stringify<Natural>(0);

    Processor<String, Natural> p = ProcessorImpl<String, Natural>(stringify);
    String one = p.process(1);
    
    void output<Y>(Y value, Processor<String,Y> p) where Y>=Number {
        log.info(p.process(value));
    }
    
    output(2,p);

}