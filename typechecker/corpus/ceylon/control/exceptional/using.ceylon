doc "Using the given resource, attempt to evaluate
     the first block."
shared Y using<X,Y>(specified X resource,
                    Y seek(coordinated X x))
        given X satisfies Usable {
    try (resource) {
        return seek(resource);
    }
}