function(e,from){
  var count=0;
  if (from===undefined||from<0)from=0;
  else if (from>=this.length)return 0;
  if (e===null) {
    for (var i=from;i<this.length;i++) {
      if (this[i]===null) count++;
    }
  } else {
    for (var i=from;i<this.length;i++) {
      if (this[i]!==null && e.equals(this[i])) count++;
    }
  }
  return count;
}
