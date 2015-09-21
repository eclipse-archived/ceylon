function (e,to) {
  if (e.size===0) return to;
  if (to===undefined)to=this.size-e.size;
  if (is$(e, {t:$_String})) {
    for (var i=to; i>=0; i--) {
      if (cmpSubString(this,e,i))return i;
    }
    return null;
  }
  else {
    return List.$$.prototype.lastInclusion.call(this,e,to);
  }
}
