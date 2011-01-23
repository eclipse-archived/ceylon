shared abstract class Optional<out X>() 
        of Something<X> | Nothing<X>
        extends Void() {
    
    doc "The unary postfix existence operator 
         |x exists|."
    shared formal Boolean defined;
    
}