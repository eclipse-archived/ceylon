function(e,from,len){
  var count=0;
  if (from===undefined||from<0)from=0;
  else if (from>=this.length)return 0;
  if (len===undefined)len=this.length-from;
  var lim=from+len;
  if(lim>this.length)lim=this.length;
  if (e===null) {
    for (var i=from;i<lim;i++) {
      if (this[i]===null) count++;
    }
  } else {
    for (var i=from;i<lim;i++) {
      if (this[i]!==null && e.equals(this[i])) count++;
    }
  }
  return count;
}
