class Bug661(){}
see(`class Bug661`)
object obj661 extends Bug661(){}

@test
shared void bug661() {
  assert(`value obj661`.annotations<SeeAnnotation>() nonempty);
  assert(exists o=`class obj661`.objectValue);
  assert(o.annotations<SeeAnnotation>() nonempty);
}
