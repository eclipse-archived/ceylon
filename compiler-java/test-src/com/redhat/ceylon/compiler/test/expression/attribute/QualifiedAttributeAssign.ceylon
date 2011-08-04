@nomodel
class QualifiedAttributeAssign(){
   variable Boolean b := true;
   shared variable Boolean b2 := true;
   QualifiedAttributeAccess q = QualifiedAttributeAccess();
   
   void m(){
       this.b := false;
       this.b2 := false;
       QualifiedAttributeAssign().b := true;
       QualifiedAttributeAssign().b2 := true;
       q.b := true;
       q.b2 := true;
   }
}