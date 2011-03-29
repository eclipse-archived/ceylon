doc "The type |Callable| represents an executable operation."
shared interface Callable<out R, P...> {
    shared formal R call(P... args);
}