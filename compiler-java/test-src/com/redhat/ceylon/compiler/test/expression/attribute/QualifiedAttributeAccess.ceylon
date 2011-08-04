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

   Boolean qm(){
       return QualifiedAttributeAccess().b;
   }

   Boolean qm2(){
       return QualifiedAttributeAccess().b2;
   }

   Boolean qm3(){
       return q.b;
   }

   Boolean qm4(){
       return q.b2;
   }
}