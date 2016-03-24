import java.lang{JString=String}
import java.util{JList=List, JArrayList=ArrayList, JSet=Set}

class Bug2054<Element>(shared Element e) {
    
}

void bug2054() {
    
    variable Boolean b;
    
    try {
        b = (Bug2054Raw() of Object) is Bug2054Java<JString, JString>;
        throw;
    } catch (Throwable e) {
        print(e.message) ;
        assert(e.message == "Cannot determine whether class com.redhat.ceylon.compiler.java.test.interop.Bug2054Raw is a com.redhat.ceylon.compiler.java.test.interop.Bug2054Java<java.lang.String,java.lang.String>");
    } 
    
    
    b = (Bug2054Complete() of Object) is Bug2054Java<JString, JString>;
    b = (Bug2054Complete() of Object) is Bug2054Java<out Anything, JString>;
    b = (Bug2054Complete() of Object) is Bug2054Java<in Nothing, JString>;
    
    try {
        b = (Bug2054Free<JString,JString>() of Object) is Bug2054Java<JString, JString>;
    } catch (Throwable e) {
        print(e.message) ;
        assert(e.message == "Cannot determine whether class com.redhat.ceylon.compiler.java.test.interop.Bug2054Free is a com.redhat.ceylon.compiler.java.test.interop.Bug2054Java<java.lang.String,java.lang.String>");
    }
    b = (Bug2054Free<JString,JString>() of Object) is Bug2054Java<out Anything, out JString>;
    b = (Bug2054Free<JString,JString>() of Object) is Bug2054Java<in Nothing, in Nothing>;
    
    try {
        b = (Bug2054Partial<JString>() of Object) is Bug2054Java<JString, JString>;
    } catch (Throwable e) {
        print(e.message) ;
        assert(e.message == "Cannot determine whether class com.redhat.ceylon.compiler.java.test.interop.Bug2054Partial is a com.redhat.ceylon.compiler.java.test.interop.Bug2054Java<java.lang.String,java.lang.String>");
    }
    b = (Bug2054PartialUpper<JString>() of Object) is Bug2054Java<out Anything, out JString>;
    //b = (Bug2054PartialUpper<JString>() of Object) is Bug2054Java<out JString, out JString>;
    b = (Bug2054IndirectUpper<JString, JString>() of Object) is Bug2054Java<in Nothing, in Nothing>;
    
    /*
    JList<String>|{String*}|JSet<String> items = JArrayList<String>(); 
    try {
        if (is JList<String> items) {
        }
        else if (is JSet<String> items) {
        }
        else {
        }
        throw;
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
    }
    
    try {
        if (is JList<String>|JSet<String> items) {
        }
        else {
        }
        throw;
    } catch (Throwable e) {
        //TODO it's confusing that we only get part of the union 
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
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
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
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
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
    }
    
    JList<String>|{String*}|JSet<String> items3 = JArrayList<String>();
    try {
        Integer sz = if (is JList<String>|JSet<String> items3) then items3.size() else items3.size;
        throw;
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
    }
    
    interface Status of Go|Abort {}
    interface Go satisfies Status {}
    interface Abort satisfies Status {}
    
    // case #1
    JList<out Status> status = JArrayList<Abort>();
    try {
        if (!is JList<Abort> status) {
            throw;
        }
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<com.redhat.ceylon.compiler.java.test.interop.bug2054_.com.redhat.ceylon.compiler.java.test.interop.bug2054$Abort_>");
    }
    // case #2
    JList<String>|JSet<String> collection = JArrayList<String>();
    try {
        if (!is JList<String> collection) {
        }
        throw;
    } catch (Throwable e) {
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.String>");
    }
    
    //
    if (!is JSet<String> collection) {
    }
    
    JList<String>|JList<Integer> collection2 = JArrayList<String>();
    try {
        if (is JList<String> collection2) {
            
        } else {
            
        }
    } catch (Throwable e) {
        print(e.message);
        // TODO why <Integer> and not <String>?
        assert(e.message == "Cannot determine whether class java.util.ArrayList is a java.util.List<ceylon.language.Integer>");
    }
     */
    // TODO check we do support is JList<out Anything> or JList<in Nothing>
    value collection3 = JArrayList<String>();
    assert (is JList<out Anything> t1=(collection3 of Object));
    assert (is JList<in Nothing> t2=(collection3 of Object));
}
