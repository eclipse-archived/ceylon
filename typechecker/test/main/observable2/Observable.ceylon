shared class Observable<Event>() 
        given Event satisfies Object {
    variable List<Anything(Nothing)> listeners = [];
    
    shared void addObserver<ObservedEvent>
            (void handle(ObservedEvent event))
            given ObservedEvent satisfies Event
            => listeners = [handle, *listeners];
    
    shared void raise<RaisedEvent>(RaisedEvent event)
            given RaisedEvent satisfies Event
            => listeners.narrow<Anything(RaisedEvent)>()
            .each((handle) => handle(event));
    
}

object observable
        extends Observable<String|Integer|Float>() {
    shared void hello() {
        raise("hello world");
    }
    shared void foo() {
        raise(10);
    }
    shared void bar() {
        raise(0.0);
    }
}

shared void runit() {
    observable.addObserver((String string) => print(string));
    observable.addObserver((Integer integer) => print(integer*integer + integer));
    observable.addObserver((Integer|Float event) => print("NUMBER"));
    
    observable.hello();
    observable.foo();
    observable.bar();
}