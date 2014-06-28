function (e) {
  if (e.size===0)return 0;
  if (is$(e, {t:$_String})) {
    for (var i=0; i < this.size-e.size+1; i++) {
      if (cmpSubString(this,e,i))return i;
    }
    return null;
  }
  else {
    return List.$$.prototype.firstInclusion.call(this,e);
  }
}
