package ceylon.language;

public interface Number {
    public ceylon.language.Natural natural();
    public ceylon.language.Integer integer();
    public ceylon.language.Whole whole();
    public ceylon.language.Float toFloat();
}
