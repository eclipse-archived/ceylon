package ceylon.language;

public abstract class Object extends Void {

    public java.lang.String toString() {
        return super.toString();
    }

    public synchronized static void addAnnotation(java.lang.Class klass,
                                                  Sequence<?> annos) {
        Types.create(klass).addAnnotations(klass, annos);
    }

    public synchronized static void addAnnotation(java.lang.Class klass,
                                                  java.lang.String memberName,
                                                  Sequence<?> annos) {
        Types.create(klass).addAnnotations(klass, annos);
    }

    public Type getType() {
        return Types.create(getClass());
    }
    
}
