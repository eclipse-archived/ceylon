@noanno
shared class Bug1135() {

    alias Listener => Callable<Anything, []>;

    variable Array<Listener> listeners = array<Listener>([]);

    shared void fireEvent() {
        listeners.map((Listener listener) => listener());
    }

}
