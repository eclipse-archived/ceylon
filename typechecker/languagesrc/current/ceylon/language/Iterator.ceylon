shared interface Iterator<out Element> {

    shared formal Element? head;
    shared formal Iterator<Element> tail;

}