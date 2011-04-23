shared class Definite<out X>(X value) 
        extends X?() {

    shared X value = value;
    
    shared actual Boolean defined { 
        return true;
    }
    
}