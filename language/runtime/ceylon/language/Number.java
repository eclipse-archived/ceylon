package ceylon.language;

public interface Number extends Format {
    public ceylon.language.Natural natural();
    public ceylon.language.Integer integer();
    public ceylon.language.Float toFloat();
}
