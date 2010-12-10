public class SimpleList<T> (T? head, SimpleList<T>? tail)
    satisfies Iterable<T>
{
    public Iterator<T> iterator() {
        return SimpleIterator<T>(this);
    }

   public class SimpleIterator<T> (SimpleList<T> ? list)
       satisfies Iterator<T>
   {
        T? head() {
            if (exists list) {
                return list.head;
            } else {
                return null;
            }
        }

        Iterator<T> tail() {
            if (exists list) {
                return SimpleIterator<T>(list.tail);
            } else {
                return SimpleIterator<T>(null);
            }
        }
    }
}
