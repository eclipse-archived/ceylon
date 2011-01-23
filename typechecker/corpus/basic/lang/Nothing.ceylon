shared class Nothing<out X>() 
        extends X?() {
        
    shared actual Boolean defined { 
        return false 
    }
    
}