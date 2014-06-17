function(c){
  if (c<=0)throw AssertionError("Step must be > 0");
  if (c===1)return this;
  var r=[],idx=0;
  while (idx<this.length) {
    r.push(this[idx]);
    idx+=c;
  }
  return r.reifyCeylonType(this._elemTarg());
}
