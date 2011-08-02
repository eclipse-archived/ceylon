@nomodel
class InitializerParamHidingGetter(Boolean b){
   Boolean b { return b; }
   Boolean b2 = b;
}