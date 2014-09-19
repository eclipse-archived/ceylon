function(i,s) {
  var end=this.v$.spanFrom(i);
  this.v$=this.v$.measure(0,i).plus(s).plus(end);
  return this;
}
