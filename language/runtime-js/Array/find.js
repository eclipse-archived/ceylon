function(f){
  for (var i=0;i<this.arr$.length;i++){
    var e=this.arr$[i];
    if (e!==null && f(e))return e;
  }
  return null;
}
