function (e) {
  if (e.size===0) return this.size;
  if (is$(e, {t:$_String})) {
    for (var i=this.size-e.size; i>=0; i--) {
      if (cmpSubString(this,e,i))return i;
    }
    return null;
  }
  else {
    return List.$$.prototype.lastInclusion.call(this,e);
  }
}
