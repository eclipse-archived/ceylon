function(f){
  for(var i=0;i<this.arr$.length;i++){
    var e=this.arr$[i];
    if (nn$(e)&&f(e))return i;
  }
  return null;
}
