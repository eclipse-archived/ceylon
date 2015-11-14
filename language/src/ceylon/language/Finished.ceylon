"The type of the value that indicates that an [[Iterator]] 
 is exhausted and has no more values to return."
see (`interface Iterator`)
tagged("Streams")
shared abstract class Finished() of finished {}

"A value that indicates that an [[Iterator]] is exhausted 
 and has no more values to return."
see (`interface Iterator`)
tagged("Streams")
shared object finished extends Finished() {
    string => "finished";
}

