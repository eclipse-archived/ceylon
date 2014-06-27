function (e) {
  if (e.size===0) return measure(0,this.size,{t:Integer});
  if (is$(e, {t:$_String})) {
    var indexes = [];
    for (var i=0; i < this.size-e.size; i++) {
      if (cmpSubString(this, e, i))indexes.push(i);
    }
    return indexes.reifyCeylonType({t:Integer});
  }
  else {
    return List.$$.prototype.inclusions.call(this,e);
  }
}
