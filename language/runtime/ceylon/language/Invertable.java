package ceylon.language;

public interface Invertable<Inverse> {
    public Inverse getPositiveValue();
    public Inverse getNegativeValue();
}
