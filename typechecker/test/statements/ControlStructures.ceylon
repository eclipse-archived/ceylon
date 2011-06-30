public class ControlStructures() {
    Object something = "hello";
    String? name = null;
    String[] names = {};
    Entry<String,String>[] entries = { Entry("hello", "world") };
    
    void print(String name) {}
    
    if (exists name) {
        print(name);
    }

    if (exists n = name) {
        print(n);
    }
    
    if (nonempty names) {
        print(names.first);
        for (n in names) {
            print(n);
        }
    }

    if (nonempty ns = names) {
        print(ns.first);
        for (n in ns) {
            print(n);
        }
    }
    
    if (is String something) {
        print(something);
    }

    if (is String string = something) {
        print(string);
    }

    for (n in names) {
        print(n);
    }
    
    /*for (value n in names) {
        print(n);
    }*/
    
    /*for (@error function n in names) {
        print(n);
    }*/
    
    for (key->item in entries) {
        print(key + "->" + item);
    }
    
    /*for (value key->value item in entries) {
        print(key + "->" + item);
    }*/
    
    class Transaction() {
        shared void rollbackOnly() {}
    }

    try (Transaction()) {}

    try (tx = Transaction()) {
        tx.rollbackOnly();
    }
    
    try {
        writeLine("hello");
    }
    catch (e) {
        
    }

    class Exception1() extends Exception() {}
    class Exception2() extends Exception() {}
    
    try {
        writeLine("hello");
    }
    catch (Exception1|Exception2 e) {
        
    }
    catch (@error String s) {
        
    }
}