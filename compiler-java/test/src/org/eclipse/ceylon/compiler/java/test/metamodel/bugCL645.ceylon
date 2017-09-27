import javax.persistence { Entity, entity }

entity
class BugCL645() {
}
shared void bugCL645() {
    assert(exists entityAnnotation = `class BugCL645`.annotations<Annotation>()[0]);
    assert(is Entity entityAnnotation);
}
