package ceylon.language;

public class doc implements ceylon.language.Annotation
{
    public static Sequence<String> run(ceylon.language.String... values) {
        return ArrayList.arrayListOf(values);
    }
}
