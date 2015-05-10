class Counter {
    Integer init;
    variable Integer count;
    shared new () {
        count = 0;
        init = count;
    }
    shared new WithInitial(Integer initial) {
        count = initial;
        init = initial;
    }
    new Clone(Counter counter) {
        count = counter.count;
        init = counter.init;
    }
    shared void inc() {
        count++;
    }
    shared void reset() {
        count = init;
    }
    shared Integer current => count;
    shared Counter clone() => Clone(this);
    shared actual String string => count.string;
}

void test() {
    @type:"Counter" value counter1 = Counter();
    @type:"Counter" value counter2 = Counter.WithInitial(1);
    @type:"Counter" value counter3 = Counter.WithInitial { initial=1; };
    @error value counter4 = Counter.Clone(counter1);
    @error /*@type:"Counter"*/ value counter5 = Counter.Counter();
    @type:"Counter" value counter6 = counter1.WithInitial(2);
    Counter();
    Counter.WithInitial(2);
    @error Counter(2);
    @error Counter.WithInitial();
    @error Counter.WithInitial("");
    @type:"Callable<Counter,Empty>" value counterFun1 = Counter;
    @type:"Callable<Counter,Tuple<Integer,Integer,Empty>>" value counterFun2 = Counter.WithInitial;
}

Float cos(Float t) => t;
Float sin(Float t) => t;

class Point2D {
    Float x; Float y;
    shared new Cartesian(Float x, Float y) {
        this.x = x; this.y = y;
    }
    shared new Polar(Float r, Float t) {
        x = r * cos(t);
        y = r * sin(t);
    }
}