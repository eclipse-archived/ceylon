doc "Represents a null reference."  
shared object null 
        extends Void() {

    shared extension object matcher 
            satisfies Matcher<Object> {
        shared actual Boolean matches(Object value) {
            return false;
        }
    }
    
    shared extension Nothing<X> nothing<X>() {
        return Nothing<X>();
    }
     
}