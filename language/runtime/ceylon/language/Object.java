package ceylon.language;

public abstract class Object extends Void {

    /** A developer-friendly string representing the instance. */
    @Extension
    public ceylon.language.String string() {
        return String.instance("");
    }

    public static void addAnnotation(Annotation ann) {
    }

    public static void addAnnotation(String memberName, Annotation ann) {
    }
}
