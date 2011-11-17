@nomodel
class InitializerParamHidingAttribute(Boolean b){
   Boolean b = b;
   Boolean b2 = b;
}
@nomodel
class InitializerParamHidingAttribute2(Boolean b){
   shared Boolean b = b;
   shared Boolean b2 = b;
}