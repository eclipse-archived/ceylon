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
    value result = ArrayList<[Object, Boolean, Boolean?, String?]>{};
    result.add([1, true, null, null]);
    result.add(["s", true, null, null]);
    result.add([true, true, true, null]);
    result.add([larger, true, true, null]);
    result.add(["c", true, null, null]);
    result.add([JavaSerializationBasic<Integer>("S", 42), true, false, null]);
    result.add([ArrayList<String>{"hello", "World"}, true, false, null]);
    result.add([HashMap<String, String>{"hello" -> "World"}, true, false, null]);
    result.add([JavaSerializationExplicit(), true, false, null]);
    result.add([JavaSerializationValueCtors.foo, true, true, null]);
    result.add([JavaSerializationValueCtors.foo.Inner.bar, true, true, null]);
    result.add([JavaSerializationValueCtors().Inner.bar, false, false, null]);
    result.add([JavaSerializationValueCtors.foo.obj, true, true, null]);
    result.add([JavaSerializationValueCtors().obj, false, false, null]);
    variable value ii = 0;
    result.add([{ii++, ii++, ii++}, false, null, "[0, 1, 2]"]);
    variable value jj = 0;
    result.add([{for (x in 1..3) x+jj++}, false, null, "[1, 3, 5]"]);
    value capture = 2;
    value f = () { assert(capture == 2); };
    result.add([f, false, null, "Anything()"]);
    return result;
}

"Compare the given original object with someting that's been 
 serialized and deserialized."
shared void javaSerializationCompare(Basic orig, Basic rt) {
    assert(is ArrayList<[Object, Boolean, Boolean?, String?]> orig);
    assert(is ArrayList<[Object, Boolean, Boolean?, String?]> rt);
    assert(!orig === rt);
    
    value origit = orig.iterator();
    value rtit = rt.iterator();
    while (is [Object, Boolean, Boolean?, String?] item=origit.next()) {
        assert(is [Object, Boolean, Boolean?, String?] rtitem = rtit.next());
        value o = item[0];
        value rto = rtitem[0];
        value eq = item[1];
        value rteq = rtitem[1];
        value id = item[2];
        value rtid = rtitem[2];
        value str = item[3];
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
        if (exists str) {
            if (rto.string != str) {
                throw AssertionError("rto.string ``rto.string`` was supposed to == ``str``, but it wasn't");
            }
        }
        if( is Anything() rto){
            rto();
        }
        
    }
    assert(rtit.next() is Finished);
}