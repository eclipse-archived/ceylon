@nomodel
class QualifiedAttributeAccess(){
   Boolean b = true;
   variable Boolean b2 := true;
   QualifiedAttributeAccess q = QualifiedAttributeAccess();
   
   Boolean m(){
       return this.b;
   }

   Boolean m2(){
       return this.b2;
   }

   Boolean m3(){
       return QualifiedAttributeAccess().b;
   }

   Boolean m4(){
       return QualifiedAttributeAccess().b2;
   }

   Boolean m5(){
       return q.b;
   }

   Boolean m6(){
       return q.b2;
   }

   Boolean m7(Foo f){
       return f.b;
   }
}
@nomodel
class Foo() {
   shared Boolean b = true;
}