class Counter {
    Integer init;
    variable Integer count;
    shared new () {
        count = 0;
        init = count;
    }
    shared new withInitial(Integer initial) {
        count = initial;
        init = initial;
    }
    new cloneMe(Counter counter) {
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
    shared Counter clone() => cloneMe(this);
    shared actual String string => count.string;
}

void test() {
    @type:"Counter" value counter1 = Counter();
    @type:"Counter" value counter2 = Counter.withInitial(1);
    @type:"Counter" value counter3 = Counter.withInitial { initial=1; };
    @error value counter4 = Counter.Clone(counter1);
    @error /*@type:"Counter"*/ value counter5 = Counter.Counter();
    @type:"Counter" value counter6 = counter1.withInitial(2);
    Counter();
    Counter.withInitial(2);
    @error Counter(2);
    @error Counter.withInitial();
    @error Counter.withInitial("");
    @type:"Callable<Counter,Empty>" value counterFun1 = Counter;
    @type:"Callable<Counter,Tuple<Integer,Integer,Empty>>" value counterFun2 = Counter.withInitial;
}

Float cos(Float t) => t;
Float sin(Float t) => t;

class Point2D {
    Float x; Float y;
    shared new cartesian(Float x, Float y) {
        this.x = x; this.y = y;
    }
    shared new polar(Float r, Float t) {
        x = r * cos(t);
        y = r * sin(t);
    }
}