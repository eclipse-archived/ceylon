shared class Counter(Integer initialCount=0) {
    variable value currentCount:=initialCount;
    shared Integer count {
        return currentCount;
    }
    shared void inc() {
        currentCount:=currentCount+1; 
    }
    shared Integer initialCount {
        return initialCount;
    }
    shared actual String string {
        return "Counter[" + count.string + "]";
    }
}

shared void test() {
    value c = Counter(0);
    print(c.count);
    c.inc(); c.inc();
    print(c.count);
    print(c);
}