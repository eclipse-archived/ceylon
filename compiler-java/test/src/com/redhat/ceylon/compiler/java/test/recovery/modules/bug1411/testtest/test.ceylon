import java.io { File }

void run() {
    File f = File(File("", ""), "");
}

/*
import java.util { LinkedList }
import ceylon.interop.java { CeylonIterator }
//import ceylon.collection { LinkedList }

//class MyList<Element>() extends LinkedList<Element>() {
//
//    shared actual Boolean containsAll(Collection<Object>? collection) => nothing; /* TODO auto-generated stub */
//    
//    shared actual Boolean empty = nothing; /* TODO auto-generated stub */
//    
//    shared actual Iterator<Element> iterator() => nothing; /* TODO auto-generated stub */
//    
//    shared actual Boolean removeAll(Collection<Object>? collection) => nothing; /* TODO auto-generated stub */
//    
//    shared actual Boolean retainAll(Collection<Object>? collection) => nothing; /* TODO auto-generated stub */
//    
//}
//

//class MyList<Element>() extends LinkedList<Element>() {
//    shared <Element|Finished>() iterFunction() {
//        return this.iterator().next;
//    }
//}

class MyList<Element>() 
        extends LinkedList<Element>() {
    shared <Element|Finished>() next() {
        value iter = CeylonIterator(iterator());
        return iter.next;
    }
}

void run() {
    value myList = MyList<String>();
    myList.add("hello");
    myList.add("world");
    value next = myList.next();
    print(next());
    print(next());
    print(next());
    print(next());
}
*/

/*
void run() {
    LinkedList<Integer> list = LinkedList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    variable Integer|Finished i;
    value next = list.iterator().next;
    while((i = next()) is Integer ) {
        print(i);
    }    
}
void run2() {    
    LinkedList<Integer> list = LinkedList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    variable Integer|Finished i;
    value iter = list.iterator();
    value next = iter.next;
    while((i = next()) is Integer ) {
        print(i);
    }
}
*/