"The result of a comparison between two [[Comparable]] 
 objects."
see (`interface Comparable`)
by ("Gavin")
shared abstract class Comparison(shared actual String string) 
        of larger | smaller | equal {
}

"The value is exactly equal to the given value."
shared object equal extends Comparison("equal") {}

"The value is smaller than the given value."
shared object smaller extends Comparison("smaller") {}

"The value is larger than the given value."
shared object larger extends Comparison("larger") {}
