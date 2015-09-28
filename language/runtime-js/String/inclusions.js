function (e,from) {
  if (e.size===0) return span(0,this.size,{t:Integer});
  if (from===undefined||from<0)from=0;
  else if (from>=this.size)return empty();
  if (is$(e, {t:$_String})) {
    var indexes = [];
    for (var i=from; i < this.size-e.size + 1; i++) {
      if (cmpSubString(this, e, i))indexes.push(i);
    }
    return indexes.rt$({t:Integer});
  }
  else {
    return SearchableList.$$.prototype.inclusions.call(this,e,from);
  }
}
