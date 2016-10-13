import java.lang{JIterable=Iterable}
import java.util{ArrayList}

void javaIterableWithSpreadOperator() {
    value al = ArrayList<String>();
    al.add("a");al.add("b");al.add("c");
    JIterable<String> it = al;
    
    assert(it*.uppercased == ["A", "B", "C"]);
    assert(it*.compare("b") == [smaller, equal, larger]);
    assert(it*.uppercased*.compare("B") == [smaller, equal, larger]);
}