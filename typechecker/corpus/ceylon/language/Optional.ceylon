shared abstract class Optional<out X>() 
        of Something<X> | null
        extends Void() {
    
    doc "The unary postfix existence operator 
         |x exists|."
    shared formal Boolean defined;
    
}