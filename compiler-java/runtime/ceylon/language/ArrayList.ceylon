public class ArrayList<T> ()  extends ArrayListImpl<T> ()
    satisfies Iterable<T>
{
    public Iterator<T> iterator() {
        return ArrayListIterator(this, 0);
    }

    public class ArrayListIterator<T> (ArrayListImpl l, Integer i)
        satisfies Iterator<T>
    {
        ArrayListImpl<T> list = l;
        Integer index = i;

        public Iterator<T> tail() {
            return ArrayListIterator(list, index + 1);
        }

        public T? head () {
            if (index >= list.length()) {
                return null;
            } else {
                return list.value(index);
            }
        }
    }
}
