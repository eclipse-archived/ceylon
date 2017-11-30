function(o){
  return is$(o,{t:AppliedMethod$jsint}) && o.tipo===this.tipo && o.typeArguments.equals(this.typeArguments);
}
