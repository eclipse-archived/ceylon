function(f) {
  this.arr$.sort(function(a,b) {
    var r = f(a,b);
    return r===larger()?1:r===smaller()?-1:0;
  });
}
