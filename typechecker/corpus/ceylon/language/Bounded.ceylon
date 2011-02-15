shared class Bounded<M>(Bounded<M> b)
       extends Object()
       satisfies Comparable<Bounded<M>>, Number
       given M satisfies Dimension {

    doc "The |Natural| representing this natural
         number."
    shared extension Natural natural { throw; }

    shared actual Comparison compare(Bounded<M> other) {
        return natural.compare(other.natural);
    }

    doc "This natural number, as a |Bounded<N>| where
         |N>=M|."
    shared extension Bounded<M+I> bounded<I>()
            given I satisfies Dimension { throw; }

}