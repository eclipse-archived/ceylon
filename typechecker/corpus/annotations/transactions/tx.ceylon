shared annotation {
    ofElements = methods;
    occurs = onceEachElement; 
}
Transactional tx(Boolean requiresNew = false) {
    return Transactional(requiresNew)
}
