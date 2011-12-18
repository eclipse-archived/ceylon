doc "The result of a comparison between two `Comparable` 
     objects."
see (Comparable)
by "Gavin"
shared abstract class Comparison(String name) 
        of larger | smaller | equal 
        extends Case(name) {}