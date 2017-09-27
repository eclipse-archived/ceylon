import java.util{JArrayList=ArrayList}
import java.lang{ObjectArray,IntArray}

void bug6143() {
    value jlist = JArrayList<String>();
    jlist.add(null);
    for (String? element in jlist) {
        print(element);
    }
    for (String element in jlist) {
        print(element+"");
    }
    for (element in jlist) {
        print(element+"");
    }
    variable Anything[] sequence = [for (String? element in jlist) element];
    sequence = [for (String element in jlist) element+""];
    sequence = [for (element in jlist) element+""];
    
    value jarray = ObjectArray<String>(1);
    jarray.set(0, null);
    for (String? element in jarray) {
        print(element);
    }
    for (String element in jarray) {
        print(element+"");
    }
    for (element in jarray) {
        print(element+"");
    }
    
    sequence = [for (String? element in jarray) element];
    sequence = [for (String element in jarray) element+""];
    sequence = [for (element in jarray) element+""];
    
    value jintarray = IntArray(1);
    jintarray.set(0, 1);
    for (Integer? element in jintarray) {
        print(element);
    }
    for (Integer element in jintarray) {
        print(element+1);
    }
    for (element in jintarray) {
        print(element+1);
    }
    
    sequence = [for (Integer? element in jintarray) element];
    sequence = [for (Integer element in jintarray) element+1];
    sequence = [for (element in jintarray) element+1];
}