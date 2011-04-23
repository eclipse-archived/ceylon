doc "The result of a comparison between two
     |Comparable| objects."
shared abstract class Comparison(String name) 
        of larger | smaller | equal 
        extends Case(name) {}