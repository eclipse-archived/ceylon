public class SimpleList<T> (T? head, SimpleList<T>? tail)
    satisfies Iterable<T>
{
    public T? car = head;
    public SimpleList<T> ? cdr = tail;

    public Iterator<T> iterator() {
        return SimpleIterator<T>(this);
    }

   public class SimpleIterator<T> (SimpleList<T> ? l)
       satisfies Iterator<T>
   {
        SimpleList<T> ? list = l;

        T? head() {
            if (exists list) {
                return list.car;
            } else {
                return null;
            }
        }

        Iterator<T> tail() {
            if (exists list) {
                return SimpleIterator<T>(list.cdr);
            } else {
                return SimpleIterator<T>(null);
            }
        }
    }
}
