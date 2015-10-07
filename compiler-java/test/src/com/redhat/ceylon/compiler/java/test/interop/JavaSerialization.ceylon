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
    
}

class JavaSerializationExplicit() satisfies Serializable {
    
}

shared Object javaSerialization() {
    value result = ArrayList<Anything>();
    
    result.add(1);
    result.add("s");
    result.add(true);
    result.add(larger);
    result.add("c");
    result.add(JavaSerializationBasic<Integer>("S", 42));
    result.add(HashMap<String, String>{"hello"->"World"});
    
    return result;
}