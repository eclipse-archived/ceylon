function(f) {
  var clone = this.clone();
  clone.sortInPlace(f);
  return ArraySequence(clone,{Element$ArraySequence:{t:this.$$targs$$.Element$Array}});
}
