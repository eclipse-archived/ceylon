doc "The type of the value that indicates that 
     an `Iterator` is exhausted and has no more 
     values to return."
see (Iterator)
shared abstract class Finished() of finished {}

doc "A value that indicates that an `Iterator`
     is exhausted and has no more values to 
     return."
see (Iterator)
shared object finished extends Finished() {
    shared actual String string => "finished";
}

