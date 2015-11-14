function(f){
  for(var i=this.length-1;i>=0;i--){
    var e=this[i];
    if (nn$(e)&&f(e))return i;
  }
  return null;
}
