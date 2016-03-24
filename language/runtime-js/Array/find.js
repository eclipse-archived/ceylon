function(f){
  for (var i=0;i<this.arr$.length;i++){
    if (f(this.arr$[i]))return this.arr$[i];
  }
  return null;
}
