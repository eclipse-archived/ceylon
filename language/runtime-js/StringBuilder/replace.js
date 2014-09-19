function(i,l,s){
  var end=this.v$.spanFrom(i+l);
  this.v$=this.v$.measure(0,i).plus(s).plus(end);
  return this;
}
