function(f){
  for(var i=this.arr$.length-1;i>=0;i--){
    var e=this.arr$[i];
    if (nn$(e)&&f(e))return i;
  }
  return null;
}
