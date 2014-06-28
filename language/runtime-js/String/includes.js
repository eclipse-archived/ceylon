function (e) {
  if (e.size===0)return true;
  if (is$(e, {t:$_String})) {
    var fi=this.firstInclusion(e);
    return fi!==null && fi>=0;
  }
  else {
    return List.$$.prototype.includes.call(this,e);
  }
}
