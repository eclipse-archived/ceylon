function (e) {
  if (e.size===0)return true;
  if (is$(e, {t:$_String})) {
    return this.firstInclusion(e) >= 0;
  }
  else {
    return List.$$.prototype.includes.call(this,e);
  }
}
