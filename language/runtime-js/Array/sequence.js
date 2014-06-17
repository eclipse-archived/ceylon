function() {
  if(this.length===0)return getEmpty();
  return ArraySequence(this,{Element$ArraySequence:this._elemTarg()});
}
