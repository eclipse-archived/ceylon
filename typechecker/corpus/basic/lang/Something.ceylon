shared extension class Something<out X>(X this) 
        extends X?() {

    shared extension X value = this;
    
    shared actual Boolean defined { 
        return true 
    }
    
}