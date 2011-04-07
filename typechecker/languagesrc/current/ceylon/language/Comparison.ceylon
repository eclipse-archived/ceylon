doc "The receiving object is larger than 
     the given object."
shared object larger extends Comparison("larger") {}

doc "The receiving object is smaller than 
     the given object."
shared object smaller extends Comparison("smaller") {}

doc "The receiving object is exactly equal 
     to the given object."
shared object equal extends Comparison("equal") {}

doc "The result of a comparison between two
     |Comparable| objects."
shared abstract class Comparison(String name) 
        of larger | smaller | equal 
        extends Case(name) {}