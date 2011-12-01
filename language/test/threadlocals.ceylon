ThreadLocal<String> val = ThreadLocal("hello");

void threadlocals() {
    assert(val.currentValue=="hello", "threalocal initial value");
    val.currentValue:="goodbye";
    assert(val.currentValue=="goodbye", "threalocal current value");
}