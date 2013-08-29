"Given a list of iterable objects, return a new sequence 
 of all elements of the all given objects. If there are
 no arguments, or if none of the arguments contains any
 elements, return the empty sequence."
see (`class SequenceBuilder`)
shared Element[] join<Element>(
        "The iterable objects to join."
        {Element*}* iterables) =>
                [ for (it in iterables) for (val in it) val ];