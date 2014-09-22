function(i,l){
  if (i>this.size)throw AssertionError('StringBuilder.delete index out of bounds');
  if (i+l>this.size)throw AssertionError('StringBuilder.delete length out of bounds');
  var end = this.v$.spanFrom(i+l);
  this.v$=this.v$.measure(0,i).plus(end);
  return this;
}
