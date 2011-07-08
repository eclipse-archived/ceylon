@nomodel
class AttributeAssign(){
   variable Boolean b := true;
   shared variable Boolean b2 := true;
   
   void m(){
       b := false;
       b2 := false;
   }
}