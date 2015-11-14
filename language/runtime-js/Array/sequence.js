function() {
  if(this.length===0)return empty();
  return ArraySequence(this,{Element$ArraySequence:this._elemTarg()});
}
