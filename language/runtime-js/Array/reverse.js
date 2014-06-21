function() {
  var clone = this.clone();
  clone.reverseInPlace();
  return ArraySequence(clone,{Element$ArraySequence:{t:this._elemTarg()}});
}
