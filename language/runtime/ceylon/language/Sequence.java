package ceylon.language;

public interface Sequence<T> {
    public T value(Integer key);
    public Integer length();
    public void $set(Integer key, T value);
    public Iterator<T> iterator();
}
