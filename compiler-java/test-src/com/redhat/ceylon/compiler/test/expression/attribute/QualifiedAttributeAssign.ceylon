@nomodel
class QualifiedAttributeAssign(){
   variable Boolean b := true;
   shared variable Boolean b2 := true;
   
   void m(){
       this.b := false;
       this.b2 := false;
       QualifiedAttributeAssign().b := true;
       QualifiedAttributeAssign().b2 := true;
   }
}