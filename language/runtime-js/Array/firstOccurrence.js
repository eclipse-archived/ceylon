function(e,from){
  if (from===undefined || from<0)from=0;
  else if (from>=this.length)return null;
  if (e===null) {
    for (var i=from;i<this.length;i++) {
      if (this[i]===null)return i;
    }
  } else {
    for (var i=from;i<this.length;i++) {
      if (this[i]!==null && e.equals(this[i]))return i;
    }
  }
  return null;
}
