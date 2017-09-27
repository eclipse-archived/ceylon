import java.util{JList=List, Arrays}

@noanno
class SpreadOptimization() {
    shared void spreadListIntoEnum() {
        List<String> list = ["a", "b", "c"];//Arrays.asList("hello", "world");
        variable value it = {*list};
        assert(it.size == 3);
        assert(!it.longerThan(4));
        assert(it.shorterThan(4));
        assert(!it.longerThan(3));
        assert(!it.shorterThan(3));
        assert(it.longerThan(2));
        assert(!it.shorterThan(2));
        assert(it.sequence() == list);
        
        it = {"a", *list};
        assert(it.size == 4);
        assert(!it.longerThan(5));
        assert(it.shorterThan(5));
        assert(!it.longerThan(4));
        assert(!it.shorterThan(4));
        assert(it.longerThan(3));
        assert(!it.shorterThan(3));
        assert(it.sequence() == ["a", "a", "b", "c"]);
        
        it = {"a", "b"};
        assert(it.size == 2);
        assert(!it.longerThan(3));
        assert(it.shorterThan(3));
        assert(!it.longerThan(2));
        assert(!it.shorterThan(2));
        assert(it.longerThan(1));
        assert(!it.shorterThan(1));
        assert(it.sequence() == ["a", "b"]);
        
        value it2 = {true, 1, -1, 1.0, -1.0, 'c', ""};
        
        value b = "b";
        it = {b, *list};
    }
    
    shared void spreadJavaListIntoEnum() {
        value list = Arrays.asList("hello", "world");
        value it = {*list};
        assert(it.size == 2);
        assert(!it.longerThan(3));
        assert(it.shorterThan(3));
        assert(!it.longerThan(2));
        assert(!it.shorterThan(2));
        assert(it.longerThan(1));
        assert(!it.shorterThan(1));
    }
    
    void f({String*} expect, {String*} args) {
        assert(expect.sequence() == args.sequence());
    }
    
    shared void spreadListIntoNamedArgs() {
        List<String> list = ["a", "b", "c"];
        f{expect=list; *list};
        f{expect=["a", *list]; "a", *list};
    }
}
void spreadOptimization() {
    SpreadOptimization().spreadListIntoEnum();
    SpreadOptimization().spreadJavaListIntoEnum();
    SpreadOptimization().spreadListIntoNamedArgs();
}