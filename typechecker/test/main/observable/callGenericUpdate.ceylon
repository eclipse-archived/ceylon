void genericUpdate<S,O>(S subject, O observer)
        given O satisfies Observer<S,O>
        given S satisfies Subject<S,O> {
    observer.update(subject);
}

void callGenericUpdate() {
    genericUpdate(Model(), Display());
}
