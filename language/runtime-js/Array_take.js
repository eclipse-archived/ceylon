function(c) {
  if (c<=0)return [].reifyCeylonType(this.$$targs$$.Element$Iterable);
  var r=this.slice(0,c);
  return this.seq$?ArraySequence(r,this.$$targs$$):r.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
