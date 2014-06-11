function(f) {
  this.sort(function(a,b) {
    var r = f(a,b);
    return r===getLarger()?1:r===getSmaller()?-1:0;
  });
}
