package ceylon.language;

public final class Character extends Object
{
    public final char value;

    private Character(char c) {
        value = c;
    }

    public static ceylon.language.Character instance(char c) {
        return new ceylon.language.Character(c);
    }

    @Extension
    public ceylon.language.String string() {
        return ceylon.language.String.instance(java.lang.Character.toString(value));
    }
}
