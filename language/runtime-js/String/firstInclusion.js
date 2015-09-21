function (e,from) {
  if (from===undefined || from<0)from=0;
  else if (from>this.size)return null;
  if (e.size===0)return from;
  if (is$(e, {t:$_String})) {
    for (var i=from; i < this.size-e.size+1; i++) {
      if (cmpSubString(this,e,i))return i;
    }
    return null;
  }
  else {
    return List.$$.prototype.firstInclusion.call(this,e,from);
  }
}
