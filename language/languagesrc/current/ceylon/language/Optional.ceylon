shared abstract class Optional<out X>() 
        of Definite<X> | null
        extends Void() {
    
    doc "The unary postfix existence operator 
         |x exists|."
    shared formal Boolean defined;
    
}