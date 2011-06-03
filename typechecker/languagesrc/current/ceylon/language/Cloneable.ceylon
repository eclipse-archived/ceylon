shared interface Cloneable<out Clone>
        given Clone satisfies Cloneable<Clone> {
    shared formal Clone clone;
}