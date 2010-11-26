package ceylon.language;

public abstract class Object extends Void {

    /** A developer-friendly string representing the instance. */
    @Extension
    public ceylon.language.String string() {
        return String.instance("");
    }

    public synchronized static void addAnnotation(java.lang.Class klass, Annotation ann) {

    }

    public static void addAnnotation(java.lang.Class klass, String memberName, Annotation ann) {
    }
}
