function(f){
  for (var i=this.arr$.length-1;i>=0;i--){
    if (f(this.arr$[i]))return this.arr$[i];
  }
  return null;
}
