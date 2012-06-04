shared Element[] emptyOrSingleton<Element>(Element? element) 
        given Element satisfies Object {
    if (exists element) {
        return Singleton(element);
    }
    else {
        return {};
    }
}