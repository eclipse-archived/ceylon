import java.util{Arrays,ArrayList}
import java.lang{ObjectArray, IntArray}

@noanno
void bug6741() {
    value expect = ArrayList<String>();
    expect.add("hello");
    expect.add("hello");
    value words = ObjectArray(2, "hello");
    variable value list = Arrays.asList(*words);
    print(list);
    assert(expect==list);
    list = Arrays.asList("bob", *words);
    expect.add(0, "bob");
    print(list);
    assert(expect==list);
    
    value expect2 = ArrayList<Integer>();
    expect2.add(42);
    expect2.add(42);
    value numbers = IntArray(2, 42);
    variable value list2 = Arrays.asList(*numbers);
    print(list2);
    assert(expect2==list2);
    expect2.add(0, 11);
    list2 = Arrays.asList(11, *numbers);
    print(list2);
    assert(expect2==list2);
}