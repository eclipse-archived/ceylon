doc "Represents a null reference."  
shared object null 
        extends Bottom?() {

    shared extension object matcher 
            satisfies Matcher<Object> {
        shared actual Boolean matches(Object value) {
            return false;
        }
    }
    
    shared actual Boolean defined { 
        return false;
    }
         
}