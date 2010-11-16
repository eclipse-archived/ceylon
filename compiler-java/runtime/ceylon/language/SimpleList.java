public class SimpleList<T> implements Iterable<T>
{
    private T car;
    private SimpleList<T> cdr;

    public T head() { return car; }
    public SimpleList<T> tail() { return cdr; }

    public Iterator<T> iterator() {
        return new SimpleListIterator(this);
    }

    public SimpleList(T head, SimpleList<T> tail) {
        car = head;
        cdr = tail;
    }

    public SimpleList(T head, Nothing tail) {
        car = head;
    }

    public SimpleList(T head) {
        car = head;
    }

    static class SimpleListIterator<T> implements Iterator<T>
    {
        Object l;

        SimpleListIterator(SimpleList<T> l) {
            this.l = l;
        }

        public Optional T head() { return (Optional<T>)l.car; }
        public SimpleListIterator<T> tail() {
            return new SimpleListIterator(l.cdr); }
    }
}
