void bug889_2<T>(T t) {
    Callable<Boolean,<T>> c = bug889(t);
    variable Boolean b := c(t);
    b := bug889(t)(t);
}