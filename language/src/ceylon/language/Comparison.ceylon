import ceylon.language { lt=larger, st=smaller, eq=equal }

doc "The result of a comparison between two `Comparable` 
     objects."
see (Comparable)
by "Gavin"
shared abstract class Comparison(shared actual String string) 
        of lt | st | eq {
    
    //TODO: remove all these:
    shared deprecated Boolean largerThan() => this == larger;    
    shared deprecated Boolean smallerThan() => this == smaller;
    shared deprecated Boolean equal() => this == equal;
    shared deprecated Boolean unequal() => this != equal;
    shared deprecated Boolean asLargeAs() => this != smaller;
    shared deprecated Boolean asSmallAs() => this != larger;

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
