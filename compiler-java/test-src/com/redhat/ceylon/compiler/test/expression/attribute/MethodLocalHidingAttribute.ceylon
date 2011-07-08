@nomodel
class MethodLocalHidingAttribute(){
   Boolean b = true;
   Boolean m(){
       Boolean b = false;
       return b;
   }
}