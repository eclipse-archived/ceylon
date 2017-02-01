shared abstract class Bug6882 of bar|bug6882_i {
    shared static object bar extends Bug6882() {}
    shared new() {}
}
object bug6882_i extends Bug6882() {}

shared void bug6882() {
    assert(Bug6882.bar in `Bug6882`.caseValues);
    assert(bug6882_i in `Bug6882`.caseValues);
}