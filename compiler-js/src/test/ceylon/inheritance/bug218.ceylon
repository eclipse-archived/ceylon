import check { check }

abstract class Bug218Base(shared String arg, String *variadic) {
    shared actual String string {
        value sb = StringBuilder().append("Bug218 arg ")
            .append(arg).append(", vars");
        for (s in variadic) {
            sb.append(" ").append(s);
        }
        return sb.string;
    }
}
class Bug218sub(String* variadic) extends Bug218Base("Test1", *variadic) {}

void bug218() {
    check(Bug218sub("a", "b", "c").string == "Bug218 arg Test1, vars a b c", "Issue 218");
}
