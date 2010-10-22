package ceylon.language;

public interface Sequence<T> {
    public T value(int key);
    public Integer length();
    public void $set(int key, T value);
}
