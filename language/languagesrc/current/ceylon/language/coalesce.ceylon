doc "Return a sequence of all elements of the given sequence
     which are not null."
shared Element[] coalesce<Element>(Element?... sequence) 
        given Element satisfies Object {
    throw;
}