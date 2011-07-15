shared interface Cloneable<out Clone> of Clone
        given Clone satisfies Cloneable<Clone> {
    shared formal Clone clone;
}