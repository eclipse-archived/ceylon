package ceylon.language;
import ceylon.language.Integer;

public class ArrayListImpl<T>
    implements Sequence<T>
{
    java.util.ArrayList<T> data;

    public Integer length() {
        return Integer.instance(data.size());
    }

    @SuppressWarnings("unchecked")
        public T value(Integer key) {
        return data.get(key.intValue());
    }

    ArrayListImpl(T[] args) {
        data = new java.util.ArrayList<T>(java.util.Arrays.asList(args));
    }

    ArrayListImpl() {
        data = new java.util.ArrayList<T>();
    }

    public ArrayListImpl(Natural initialCapacity) {
        System.out.println(initialCapacity.intValue());
        data = new java.util.ArrayList<T>(initialCapacity.intValue());
     }

    public static <N> ArrayListImpl<N> of (N... args) {
        return new ArrayListImpl<N>(args);
    }

    public ArrayListImpl<T> append(ArrayListImpl<T> l) {
        ArrayListImpl<T> newList = newInstance();

        newList.data = new java.util.ArrayList<T>(data) ;
        if (l != null) {
            newList.data.addAll(l.data);
        }

        return newList;
    }

    protected ArrayListImpl<T> newInstance() {
        try {
            return getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void $set(Integer key, T value) {
        data.add(key.intValue(), value);
    }

    public void set(Integer key, T value) {
        data.add(key.intValue(), value);
    }

    public static <T> ArrayList<T> arrayListOf(T... args) {
        ArrayList l = new ArrayList();
        l.data = new java.util.ArrayList<T>(java.util.Arrays.asList(args));
        return l;
    }

    public Iterator<T> iterator() {
        throw new RuntimeException(this.toString());
    }
}
