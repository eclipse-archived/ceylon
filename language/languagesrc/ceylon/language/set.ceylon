shared T set<T>(Settable<T> value)(T newValue) { 
    return value := newValue;
}