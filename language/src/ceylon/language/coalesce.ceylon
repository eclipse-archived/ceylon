doc "Return a sequence containing the given values which are
     not null. If there are no values which are not null,
     return an empty sequence."
shared Element[] coalesce<Element>(Element?... values) 
        given Element satisfies Object {
    return { for (val in values) if (exists val) val };
}