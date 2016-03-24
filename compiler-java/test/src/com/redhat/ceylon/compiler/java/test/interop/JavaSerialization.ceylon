import ceylon.collection{
    ArrayList,
    HashMap
}
import java.io{
    Serializable
}

class JavaSerializationBasic<T>(String s, T t) 
        given T satisfies Object {
    
    shared actual Boolean equals(Object other) {
        if (is JavaSerializationBasic<T> other) {
            return this.s == other.s
                && this.t == other.t;
        }
        return false;
    }
    shared actual Integer hash => this.s.hash ^ this.t.hash;
}

class JavaSerializationExplicit() satisfies Serializable {
    shared actual Boolean equals(Object other) {
        return other is JavaSerializationExplicit;
    }
    shared actual Integer hash = 0;
}

class JavaSerializationValueCtors {
    String s;
    shared new() {
        s = "default ctor";
    }
    shared new foo {
        s = "foo";
    }
    shared actual String string => "``super.string``` (``s``)";
    
    shared class Inner {
        //shared actual String string;
        shared new bar {
            //string = "bar";
        }
    }
    
    shared object obj {
        
    }
}

"Return an object to serialize"
shared Object javaSerialization() {
                                   // expect ==, expect ===
    value result = ArrayList<[Object, Boolean, Boolean?]>{};
    
    result.add([1, true, null]);
    result.add(["s", true, null]);
    result.add([true, true, true]);
    result.add([larger, true, true]);
    result.add(["c", true, null]);
    result.add([JavaSerializationBasic<Integer>("S", 42), true, false]);
    result.add([ArrayList<String>{"hello", "World"}, true, false]);
    result.add([HashMap<String, String>{"hello" -> "World"}, true, false]);
    result.add([JavaSerializationExplicit(), true, false]);
    result.add([JavaSerializationValueCtors.foo, true, true]);
    result.add([JavaSerializationValueCtors.foo.Inner.bar, true, true]);
    result.add([JavaSerializationValueCtors().Inner.bar, false, false]);
    result.add([JavaSerializationValueCtors.foo.obj, true, true]);
    result.add([JavaSerializationValueCtors().obj, false, false]);
    return result;
}

"Compare the given original object with someting that's been 
 serialized and deserialized."
shared void javaSerializationCompare(Basic orig, Basic rt) {
    assert(is ArrayList<[Object, Boolean, Boolean?]> orig);
    assert(is ArrayList<[Object, Boolean, Boolean?]> rt);
    assert(!orig === rt);
    
    value origit = orig.iterator();
    value rtit = rt.iterator();
    while (is [Object, Boolean, Boolean?] item=origit.next()) {
        assert(is [Object, Boolean, Boolean?] rtitem = rtit.next());
        value o = item[0];
        value rto = rtitem[0];
        value eq = item[1];
        value rteq = rtitem[1];
        value id = item[2];
        value rtid = rtitem[2];
        if (eq) {
            assert(rteq);
            if (eq) {
                if(o != rto) {
                    throw AssertionError("``o`` was supposed to be == ``rto``, but it wasn't");
                }
            } else {
                if(o == rto) {
                    throw AssertionError("``o`` was supposed to be != ``rto``, but it was ==");
                }
            }
        } else {
            assert(!rteq);
        }
        if (exists id) {
            assert(exists rtid);
            assert(id==rtid);
            assert(is Identifiable o);
            assert(is Identifiable rto);
            if (id) {
                if (!(o === rto)) {
                    throw AssertionError("``o`` was supposed to be === ``rto``, but it wasn't");
                }
            } else {
                if (o === rto) {
                    throw AssertionError("``o`` was supposed to be !== ``rto``, but it was ===");
                }
            }
        } else {
            assert(!rtid exists);
        }
        
    }
    assert(rtit.next() is Finished);
}