// Check we don't reeval the iterable expression
void bug1351_reeval() {
    variable Integer end = 3;
    Iterable<Integer> foo {
        end++;
        object bar satisfies Iterable<Integer> {
            shared actual Iterator<Integer> iterator() {
                return (0..end).iterator();
            }
        }
        return bar;
    }
    value lala = {for (x in foo) x};
    assert ([*lala] == [*lala]);
}
// Check we can handle dependent iterables
shared Element[] bug1351_dependant<Element>({Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];


// Check we don't fuck up when we're in an initializer, or a super
class Bug1351BackwardsBranch(Integer* it) {
    assert([for (num in it*.string) parseInteger(num)] == [*it]);
}
// and we use a sread as an iterable expr in a comprehension
class Bug1351BackwardsBranchSuper() extends Bug1351BackwardsBranch(for (num in 0..10) num+1) {
    
}

void bug1351() {
    bug1351_reeval();
    assert(["", "a"] == bug1351_dependant({}, {""}, {"a"}));
    Bug1351BackwardsBranchSuper();
}