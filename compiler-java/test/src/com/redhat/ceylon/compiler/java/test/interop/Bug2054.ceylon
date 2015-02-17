import java.util{JList=List, JArrayList=ArrayList, JSet=Set}

class Bug2054<Element>(shared Element e) {
    
}

void bug2054() {
    JList<String>|{String*}|JSet<String> items = JArrayList<String>(); 
    try {
        if (is JList<String> items) {
        }
        else if (is JSet<String> items) {
        }
        else {
        }
        throw;
    } catch (AssertionError e) {
        assert(e.message == "Generic Java type could not satisfy is condition due to erasure of type arguments");
    }
    
    try {
        if (is JList<String>|JSet<String> items) {
        }
        else {
        }
        throw;
    } catch (AssertionError e) {
        assert(e.message == "Generic Java type could not satisfy is condition due to erasure of type arguments");
    }
    
    if (is {String*} items) {
    }
    else {
    }
    
    alias Foo => JList<String>|JSet<String>;
    try {
        if (is Foo items) {
        }
        else {
        }
        throw;
    } catch (AssertionError e) {
        assert(e.message == "Generic Java type could not satisfy is condition due to erasure of type arguments");
    }
    
    value strings = JArrayList<String>();
    strings.add("hello");
    Bug2054<JList<String>>|Bug2054<JList<Integer>> tp = Bug2054<JList<String>>(strings);
    if (is Bug2054<JList<String>> tp) {
        String s = tp.e.get(0);
    } else {
        Integer s = tp.e.get(0);
    }

    JList<String>|{String*} items2 = JArrayList<String>();
    try {
        Integer sz = if (is JList<String> items2) then items2.size() else items2.size;
        throw;
    } catch (AssertionError e) {
        assert(e.message == "Generic Java type could not satisfy is condition due to erasure of type arguments");
    }
    
    JList<String>|{String*}|JSet<String> items3 = JArrayList<String>();
    try {
        Integer sz = if (is JList<String>|JSet<String> items3) then items3.size() else items3.size;
        throw;
    } catch (AssertionError e) {
        assert(e.message == "Generic Java type could not satisfy is condition due to erasure of type arguments");
    } 
}
