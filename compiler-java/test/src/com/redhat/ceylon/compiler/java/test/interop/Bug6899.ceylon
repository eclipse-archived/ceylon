import java.util{JList=List,
    JArrayList=ArrayList}

shared void bug6899() {
    JList<String> | JList<Integer> al = JArrayList<String>();
    print({*al});
}