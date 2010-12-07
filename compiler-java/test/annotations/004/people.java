import ceylon.language.*;

// Written in Java until we have varargs support for Ceylon
public class people implements ceylon.language.Annotation
{
    public static Sequence<Person> run(Person... values) {
        return ArrayList.arrayListOf(values);
    }
}
