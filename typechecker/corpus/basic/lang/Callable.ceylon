shared interface Callable<out R, P...> {
    shared formal R call(P... args);
}