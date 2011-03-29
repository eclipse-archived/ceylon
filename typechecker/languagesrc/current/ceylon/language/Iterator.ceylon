shared interface Iterator<out X> {

    shared formal X? head;
    shared formal Iterator<X> tail;

}