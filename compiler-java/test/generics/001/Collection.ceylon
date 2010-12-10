public class Collection<T> ()
    satisfies Iterable<T>
    given T satisfies Vehicle {

    mutable SimpleList<T> vehicles = SimpleList(null, null);

    void add(T vehicle) {
        vehicles := SimpleList(vehicle, vehicles);
    }

    public Iterator<T> iterator() {
        return vehicles.iterator();
    }
}
