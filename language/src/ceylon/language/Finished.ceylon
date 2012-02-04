doc "The type of the value that indicates that 
     an `Iterator` is exhausted and has no more 
     values to return."
see (Iterator)
shared abstract class Finished() of exhausted {}

doc "A value that indicates that an `Iterator`
     is exhausted and has no more values to 
     return."
see (Iterator)
shared object exhausted extends Finished() {
    shared actual String string { 
        return "exhausted";
    }
}

