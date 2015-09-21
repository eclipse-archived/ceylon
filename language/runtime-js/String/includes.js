function (e,from) {
  if (e.size===0)return true;
  if (from===undefined||from<0)from=0;
  else if (from>=this.size)return false;
  if (is$(e, {t:$_String})) {
    var fi=this.firstInclusion(e,from);
    return fi!==null && fi>=0;
  }
  else {
    return List.$$.prototype.includes.call(this,e,from);
  }
}
