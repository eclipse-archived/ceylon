@nomodel
class MethodParamHidingAttribute(){
   Boolean b = true;
   void m(Boolean b){
       Boolean b2 = b;
   }
}