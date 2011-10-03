package ceylon.language;

public interface Category {
    public boolean contains(Object value);
    
    public boolean containsEvery(ceylon.language.Iterable<? extends Object> elements);

    public boolean containsAny(ceylon.language.Iterable<? extends Object> elements);
}
