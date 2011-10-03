package ceylon.language;

/**
 * Class to facilitate method calls using named arguments. This will be 
 * removed/replace after M1.
 *
 * @param <R> The type of result
 * @param <X> The type of the thing being called
 */
public abstract class NamedArgumentCall<X> {
    protected final X instance;
    protected final java.lang.Object[] args;
    public NamedArgumentCall(final X instance, java.lang.Object... args) {
        this.instance = instance;
        this.args = args;
    }
}