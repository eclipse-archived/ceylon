function (e,from) {
  if (e.size===0) return measure(0,this.size,{t:Integer});
  if (from===undefined||from<0)from=0;
  else if (form>=this.size)return empty();
  if (is$(e, {t:$_String})) {
    var indexes = [];
    for (var i=from; i < this.size-e.size + 1; i++) {
      if (cmpSubString(this, e, i))indexes.push(i);
    }
    return indexes.rt$({t:Integer});
  }
  else {
    return List.$$.prototype.inclusions.call(this,e,from);
  }
}
