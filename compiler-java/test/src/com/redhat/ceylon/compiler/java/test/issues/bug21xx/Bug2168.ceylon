import ceylon.language.meta.model { Method, Function }

class MethodFunction( produce) {
    
    shared Method<Anything, Boolean?, [List<String>]> |
            Function<Boolean?, [List<String>, String]> produce;
}

class Call(MethodFunction mf) {
    shared Boolean? produceRoute(List<String> c, MethodFunction r) {
        if ( is Method<Anything,Boolean?,[List<String>]> producer = r.produce) {
            return producer("")(c); 
        }
        else if ( is Function<Boolean?,[List<String>, String]> producer = r.produce) { 
            return producer(c, "");                    
        } else {
            return null;
        }
    }
}
