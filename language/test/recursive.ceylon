class Recursive(Integer int) 
        satisfies Enumerable<Recursive> {
    assert (0<=int<=5);
    neighbour(Integer offset) => Recursive((int+offset)%5);
    offset(Recursive other) 
            => (other.int>int then int+5 else int)-other.int;
    shared actual Boolean equals(Object that) {
        if (is Recursive that) {
            return int==that.int;
        }
        else {
            return false;
        }
    }
    shared actual Integer hash {
        variable value hash = 1;
        hash = 31*hash + int.hash;
        return hash;
    }
    string => int.string;
}

@test
shared void testRecursiveRange() {
    function id(Recursive x) => x;
    check((Recursive(2)..Recursive(2)).collect(id).string=="[2]", "recursive range 0");
    check((Recursive(1)..Recursive(4)).collect(id).string=="[1, 2, 3, 4]", "recursive range 1");
    check((Recursive(4)..Recursive(1)).collect(id).string=="[4, 0, 1]", "recursive range 2");
    check((Recursive(3)..Recursive(2)).collect(id).string=="[3, 4, 0, 1, 2]", "recursive range 3");
    check((Recursive(2):0).collect(id).string=="[]", "recursive range 4");
    check((Recursive(1):3).collect(id).string=="[1, 2, 3]", "recursive range 5");
    check((Recursive(4):3).collect(id).string=="[4, 0, 1]", "recursive range 6");
    check((Recursive(1):15).collect(id).string=="[1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0]", "recursive range 7");
    check(((Recursive(4):3).first else "")==Recursive(4), "recursive measure first");
    check(((Recursive(4):3).last else "")==Recursive(1), "recursive measure last");
    check(((Recursive(1):15).first else "")==Recursive(1), "recursive measure first");
    check(((Recursive(1):15).last else "")==Recursive(0), "recursive measure last");
    check(Recursive(0) in Recursive(4)..Recursive(1), "recursive range in");
    check(!Recursive(3) in Recursive(4)..Recursive(1), "recursive range not in");
}