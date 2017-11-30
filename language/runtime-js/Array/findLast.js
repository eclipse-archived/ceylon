function(f){
  for (var i=this.arr$.length-1;i>=0;i--){
    var e=this.arr$[i];
    if (e!==null && f(e))return e;
  }
  return null;
}
