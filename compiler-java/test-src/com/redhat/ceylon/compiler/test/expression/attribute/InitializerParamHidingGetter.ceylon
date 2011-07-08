@nomodel
class InitializerParamHidingGetter(Boolean b){
   Boolean b { return b; }
   b.equals(true);
}