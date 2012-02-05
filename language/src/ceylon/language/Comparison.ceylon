doc "The result of a comparison between two `Comparable` 
     objects."
see (Comparable)
by "Gavin"
shared abstract class Comparison(String name) 
        of larger | smaller | equal {
    shared actual String string {
        return name;
    }
}

doc "The value is exactly equal to the given value."
by "Gavin"
shared object equal extends Comparison("equal") {}

doc "The value is smaller than the given value."
by "Gavin"
shared object smaller extends Comparison("smaller") {}

doc "The value is larger than the given value."
by "Gavin"
shared object larger extends Comparison("larger") {}
