@noanno
class SmallIteration() {
    shared void measure() {
        Integer start = 0;
        small Integer smallLength = 10;
        for (i in start:smallLength) {
            print(i);
        }
        
        small Integer smallStart = 0;
        for (i in smallStart:smallLength) {
            print(i);
        }
        
        Integer length = 10;
        for (i in smallStart:length) {
            print(i);
        }
        
        variable Integer sum = 0;
        @disableOptimization
        for (i in smallStart:smallLength) {
            sum+=i;
        }
        //for (c in 'a':26) {
        //    sum += c.integer;
        //}
    }
    shared void span() {
        Integer start = 0;
        Integer end = 10;
        for (i in start..end) {
            print(i);
        }
    }
}