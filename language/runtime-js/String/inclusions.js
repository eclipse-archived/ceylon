function (e) {
  if (e.size===0) return measure(0,this.size,{t:Integer});
  if (is$(e, {t:$_String})) {
    var indexes = [];
    for (var i=0; i < this.size-e.size + 1; i++) {
      if (cmpSubString(this, e, i))indexes.push(i);
    }
    return indexes.rt$({t:Integer});
  }
  else {
    return List.$$.prototype.inclusions.call(this,e);
  }
}
