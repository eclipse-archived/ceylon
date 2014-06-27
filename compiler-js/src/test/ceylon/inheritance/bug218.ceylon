import check { check }

abstract class Bug218Base(shared String arg, String *variadic) {
    shared actual String string {
        return "Bug218 arg ``arg``, vars " + " ".join(variadic);
    }
}
class Bug218sub(String* variadic) extends Bug218Base("Test1", *variadic) {}
class Bug218sub2(String* variadic) extends Bug218Base("Test2", "ADD", *variadic) {}
class Bug218sub3(String* variadic) extends Bug218Base("Test3") {}

void bug218() {
    check(Bug218sub("a", "b", "c").string == "Bug218 arg Test1, vars a b c", "Issue 218 [1]");
    check(Bug218sub2("a", "b", "c").string == "Bug218 arg Test2, vars ADD a b c", "Issue 218 [2]");
    check(Bug218sub3("a", "b", "c").string == "Bug218 arg Test3, vars ", "Issue 218 [3]");
}
