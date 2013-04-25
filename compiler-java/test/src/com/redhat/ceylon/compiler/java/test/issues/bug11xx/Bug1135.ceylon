@nomodel
shared class Bug1135<Args>() given Args satisfies Anything[] {

    alias Listener => Callable<Anything, Args>;

    variable Array<Listener> listeners = array<Listener>([]);

    shared void fireEvent(Args args) {
        listeners.map((Listener listener) => listener(args));
    }

}
