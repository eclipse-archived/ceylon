function (e,from) {
  //TODO: optimize!
  if (from===undefined||from<0)from=0;
  else if (from>=this.size)return 0;
  return List.$$.prototype.countOccurrences.call(this,e,from);
}
