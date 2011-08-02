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

    public ceylon.language.Character lowercase() {
        return instance(java.lang.Character.toLowerCase(value));
    }

    public ceylon.language.Character uppercase() {
        return instance(java.lang.Character.toUpperCase(value));
    }

    public java.lang.String toString() {
        return java.lang.Character.toString(value);
    }
}
