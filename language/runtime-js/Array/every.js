function(f) {
  for (var i=0;i<this.arr$.length;i++){
    if (!f(this.arr$[i]))return false;
  }
  return true;
}
