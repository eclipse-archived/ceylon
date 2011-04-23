doc "Represents a null reference."  
shared object null 
        extends Bottom?() {
    
    shared actual Boolean defined { 
        return false;
    }
         
}