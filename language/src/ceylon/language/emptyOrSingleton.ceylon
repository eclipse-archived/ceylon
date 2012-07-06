doc "A `Singleton` if the given element is non-null, otherwise `Empty`."
see Singleton
see Empty
shared Element[] emptyOrSingleton<Element>(Element? element) 
        given Element satisfies Object {
    if (exists element) {
        return Singleton(element);
    }
    else {
        return {};
    }
}