function(e,from,len){
  if (from===undefined || from<0)from=0;
  else if (from>=this.length)return null;
  if (len===undefined)len=this.length;
  if (e===null) {
    for (var i=from;i<len;i++) {
      if (this[i]===null)return i;
    }
  } else {
    for (var i=from;i<len;i++) {
      if (this[i]!==null && e.equals(this[i]))return i;
    }
  }
  return null;
}
