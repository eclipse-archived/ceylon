@nomodel
variable Boolean toplevel := true;

@nomodel
class TopLevelAssign(){
   void m(){
       toplevel := false;
   }
}