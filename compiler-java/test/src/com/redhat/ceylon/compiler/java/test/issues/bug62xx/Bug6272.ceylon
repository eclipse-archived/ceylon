import ceylon.collection {
    HashMap
}

class Bug6272() {
    
    Map<String, String> handlers = HashMap{
        "foo" -> bar
    };
    
    shared void recursing(){
        assert(exists handler = handlers.get(""));
    }
    
}