function (e,from,len) {
  //TODO: optimize!
  if (from===undefined||from<0)from=0;
  else if (from>=this.size)return 0;
  if (len===undefined)len=this.size-from;
  return SearchableList.$$.prototype.countOccurrences.call(this,e,from,len);
}
