function(i,l){
  var end = this.v$.spanFrom(i+l);
  this.v$=this.v$.measure(0,i).plus(end);
  return this;
}
