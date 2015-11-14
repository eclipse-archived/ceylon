function(f){
  for(var i=0;i<this.length;i++){
    var e=this[i];
    if (nn$(e)&&f(e))return i;
  }
  return null;
}
