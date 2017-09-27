unknownAnnotation
class UnknownAnnotation(unknownAnnotation Anything a) {
    shared unknownAnnotation
    void m(unknownAnnotation Anything a) {}
    
    shared unknownAnnotation
    Integer b => 1;
}
unknownAnnotation
void unknownAnnotations(unknownAnnotation Anything(Integer) progress) {
    progress(1);
    value ua = UnknownAnnotation(null);
    ua.m(null);
    value b = ua.b;
    progress(2);
}