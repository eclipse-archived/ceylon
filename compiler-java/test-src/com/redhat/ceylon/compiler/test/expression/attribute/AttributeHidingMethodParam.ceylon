class AttributeHidingMethodParam(){
   Boolean b = true;
   Boolean m(Boolean b){
       return this.b;
   }
}