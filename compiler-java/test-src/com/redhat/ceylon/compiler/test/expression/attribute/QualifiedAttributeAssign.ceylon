@nomodel
class QualifiedAttributeAssign(){
   variable Boolean b := true;
   shared variable Boolean b2 := true;
   QualifiedAttributeAssign q = QualifiedAttributeAssign();
   
   void m(Foo f){
       this.b := false;
       this.b2 := false;
       QualifiedAttributeAssign().b := true;
       QualifiedAttributeAssign().b2 := true;
       q.b := q.b2;
       f.b := f.b;
   }
}
@nomodel
class Foo() {
   shared variable Boolean b := true;
}