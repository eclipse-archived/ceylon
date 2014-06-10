function(c){
  if (c<=0)return this;
  var r=this.slice(c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
