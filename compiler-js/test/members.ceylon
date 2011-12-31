class Counter(Integer initialCount=0) {
    variable value currentCount:=initialCount;
    shared Integer count {
        return currentCount;
    }
    shared void inc() {
       currentCount:=currentCount+1; 
    }
}