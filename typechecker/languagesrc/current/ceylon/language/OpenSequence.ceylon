shared interface OpenSequence<X>
        satisfies X[] & OpenCorrespondence<Natural,X>
        given X satisfies Equality<X> {
}