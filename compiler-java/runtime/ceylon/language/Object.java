package ceylon.language;

public abstract class Object extends Void {

    /** A developer-friendly string representing the instance. */
    @Extension
    public ceylon.language.String string() {
        return String.instance("");
    }

    public synchronized static void addAnnotation(java.lang.Class klass,
                                                  Sequence<?> annos) {
        Types.create(klass).addAnnotations(klass, annos);
    }

    public synchronized static void addAnnotation(java.lang.Class klass,
                                         String memberName,
                                         Sequence<?> annos) {
        Types.create(klass).addAnnotations(klass, annos);
    }

    public Type getType () {
        return Types.create(getClass());
    }
}
